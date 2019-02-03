package com.lelloman.simplerss.feed

import com.lelloman.simplerss.persistence.settings.AppSettings.Companion.DEFAULT_MIN_SOURCE_REFRESH_INTERVAL
import com.lelloman.simplerss.testutils.MockLogger
import com.lelloman.simplerss.testutils.MockLoggerFactory
import com.lelloman.simplerss.testutils.MockTimeProvider
import com.lelloman.simplerss.testutils.dummyArticle
import com.lelloman.simplerss.testutils.dummySource
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers.trampoline
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.junit.Test

class FeedRefresherImplTest {

    private val sourcesDao: com.lelloman.simplerss.persistence.db.SourcesDao = mock()
    private val articlesDao: com.lelloman.simplerss.persistence.db.ArticlesDao = mock()
    private val timeProvider = MockTimeProvider()
    private val appSettings = com.lelloman.simplerss.mock.MockAppSettings()
    private val logger = MockLogger()
    private val loggerFactory = MockLoggerFactory(logger)
    private val feedFetcher: com.lelloman.simplerss.feed.fetcher.FeedFetcher = mock()
    private val faviconFetcher: com.lelloman.simplerss.feed.fetcher.FaviconFetcher = mock()

    private val dependencies = arrayOf(feedFetcher, sourcesDao, articlesDao)

    private val tested = com.lelloman.simplerss.feed.FeedRefresherImpl(
        ioScheduler = trampoline(),
        newThreadScheduler = trampoline(),
        httpPoolScheduler = trampoline(),
        sourcesDao = sourcesDao,
        articlesDao = articlesDao,
        timeProvider = timeProvider,
        appSettings = appSettings,
        loggerFactory = loggerFactory,
        feedFetcher = feedFetcher,
        faviconFetcher = faviconFetcher
    )

    @Test
    fun `is not loading when instantiated`() {
        val tester = tested.isLoading.test()

        tester.assertNotComplete()
        tester.assertNoErrors()
        tester.assertValues(false)
    }

    @Test
    fun `starts loading when refresh is called`() {
        givenAllSourcesIsEmpty()
        givenActiveSourcesSubject()
        givenHasDefaultMinRefreshInterval()
        val tester = tested.isLoading.test()

        tested.refresh()

        tester.assertValues(false, true)
    }

    @Test
    fun `does nothing if refresh is called while loading`() {
        givenAllSourcesIsEmpty()
        givenActiveSourcesSubject()
        givenHasDefaultMinRefreshInterval()
        val tester = tested.isLoading.test()
        tested.refresh()
        reset(*dependencies)

        tested.refresh()

        verifyZeroInteractions(*dependencies)
        tester.assertValues(false, true)
    }

    @Test
    fun `stops loading when finish refresh successfully`() {
        givenAllSourcesIsEmpty()
        val sourcesSubject = givenActiveSourcesSubject()
        val tester = tested.isLoading.test()
        givenHasDefaultMinRefreshInterval()

        tested.refresh()
        sourcesSubject.onNext(emptyList())

        tester.awaitCount(3)
        tester.assertValues(false, true, false)
    }

    @Test
    fun `stops loading when finish refresh with error`() {
        givenAllSourcesIsEmpty()
        val sourcesSubject = givenActiveSourcesSubject()
        val tester = tested.isLoading.test()
        givenHasDefaultMinRefreshInterval()

        tested.refresh()
        sourcesSubject.onError(Exception("meow"))

        tester.assertValues(false, true, false)
    }

    @Test
    fun `refreshes stale sources only`() {
        givenAllSourcesIsEmpty()
        val now = 12345L
        val minRefreshInterval = com.lelloman.simplerss.persistence.settings.SourceRefreshInterval.NEUROTIC
        givenHasMinRefreshInterval(minRefreshInterval)
        whenever(feedFetcher.fetchFeed(any())).thenAnswer {
            Maybe.just(it.arguments[0] as com.lelloman.simplerss.persistence.db.model.Source to emptyList<com.lelloman.simplerss.persistence.db.model.Article>())
        }
        givenHasTime(now)
        val staleSource = com.lelloman.simplerss.persistence.db.model.Source(id = 1L,
            name = "stale source",
            url = "url 1",
            lastFetched = now - (minRefreshInterval.ms * 2),
            isActive = true
        )
        val updatedSource = com.lelloman.simplerss.persistence.db.model.Source(id = 2L,
            name = "updated source",
            url = "url 2",
            lastFetched = now - (minRefreshInterval.ms / 2),
            isActive = true
        )
        givenHasActiveSources(staleSource, updatedSource)
        val tester = tested.isLoading.test()

        tested.refresh()

        verify(feedFetcher).fetchFeed(staleSource)
        verify(feedFetcher, never()).fetchFeed(updatedSource)
        tester.assertValues(false, true, false)
    }

    @Test
    fun `silently drops feed fetcher errors`() {
        givenAllSourcesIsEmpty()
        whenever(feedFetcher.fetchFeed(any())).thenReturn(Maybe.error(Exception()))
        givenHasTime(Long.MAX_VALUE)
        givenHasMinRefreshInterval(com.lelloman.simplerss.persistence.settings.SourceRefreshInterval.NEUROTIC)
        givenHasActiveSources(com.lelloman.simplerss.feed.FeedRefresherImplTest.Companion.SOURCE_1)

        tested.refresh()

        logger.assertNeverLoggedError()
    }

    @Test
    fun `logs warning when fetching active sources`() {
        givenAllSourcesIsEmpty()
        givenHasDefaultMinRefreshInterval()
        val error = Exception("crasc")
        whenever(sourcesDao.getActiveSources()).thenReturn(Flowable.error(error))

        tested.refresh()

        logger.assertLoggedWarning(com.lelloman.simplerss.feed.FeedRefresherImplTest.Companion.GENERIC_ERR_MSG, error)
    }

    @Test
    fun `updates db when fetched feed successfully`() {
        givenAllSourcesIsEmpty()
        givenHasActiveSources(com.lelloman.simplerss.feed.FeedRefresherImplTest.Companion.SOURCE_1, com.lelloman.simplerss.feed.FeedRefresherImplTest.Companion.SOURCE_2)
        givenHasTime(Long.MAX_VALUE)
        givenHasMinRefreshInterval(com.lelloman.simplerss.persistence.settings.SourceRefreshInterval.NEUROTIC)
        val articles1 = listOf(dummyArticle())
        val articles2 = listOf(dummyArticle(), dummyArticle())
        whenever(feedFetcher.fetchFeed(com.lelloman.simplerss.feed.FeedRefresherImplTest.Companion.SOURCE_1)).thenReturn(Maybe.just(com.lelloman.simplerss.feed.FeedRefresherImplTest.Companion.SOURCE_1 to articles1))
        whenever(feedFetcher.fetchFeed(com.lelloman.simplerss.feed.FeedRefresherImplTest.Companion.SOURCE_2)).thenReturn(Maybe.just(com.lelloman.simplerss.feed.FeedRefresherImplTest.Companion.SOURCE_2 to articles2))

        tested.refresh()

        verify(articlesDao).deleteArticlesFromSource(com.lelloman.simplerss.feed.FeedRefresherImplTest.Companion.SOURCE_1.id)
        verify(articlesDao).deleteArticlesFromSource(com.lelloman.simplerss.feed.FeedRefresherImplTest.Companion.SOURCE_2.id)
        verify(articlesDao).insertAll(eq(articles1[0]))
        verify(articlesDao).insertAll(eq(articles2[0]), eq(articles2[1]))
        verify(sourcesDao).updateSourceLastFetched(com.lelloman.simplerss.feed.FeedRefresherImplTest.Companion.SOURCE_1.id, timeProvider.nowUtcMs())
        verify(sourcesDao).updateSourceLastFetched(com.lelloman.simplerss.feed.FeedRefresherImplTest.Companion.SOURCE_2.id, timeProvider.nowUtcMs())
    }

    @Test
    fun `downloads favicons on refresh`() {
        givenHasActiveSources()
        givenHasMinRefreshInterval(com.lelloman.simplerss.persistence.settings.SourceRefreshInterval.NEUROTIC)
        val allSources = listOf(
            dummySource(index = 1).copy(favicon = ByteArray(0)),
            dummySource(index = 2).copy(favicon = null)
        )
        whenever(sourcesDao.getAll()).thenReturn(Flowable.just(allSources))
        val bytes = byteArrayOf(1, 2, 3)
        whenever(faviconFetcher.getPngFavicon(any())).thenReturn(Maybe.just(bytes))

        tested.refresh()

        verify(faviconFetcher).getPngFavicon(allSources[1].url)
    }

    private fun givenHasDefaultMinRefreshInterval() {
        appSettings.providedSourceRefreshMinInterval = Observable.just(DEFAULT_MIN_SOURCE_REFRESH_INTERVAL)
    }

    private fun givenHasMinRefreshInterval(sourceRefreshInterval: com.lelloman.simplerss.persistence.settings.SourceRefreshInterval) {
        appSettings.providedSourceRefreshMinInterval = Observable.just(sourceRefreshInterval)
    }

    private fun givenHasActiveSources(vararg source: com.lelloman.simplerss.persistence.db.model.Source) {
        whenever(sourcesDao.getActiveSources()).thenReturn(Flowable.just(source.toList()))
    }

    private fun givenHasTime(time: Long) {
        timeProvider.now = time
    }

    private fun givenActiveSourcesSubject(): Subject<List<com.lelloman.simplerss.persistence.db.model.Source>> {
        val sourcesSubject = PublishSubject.create<List<com.lelloman.simplerss.persistence.db.model.Source>>()
        whenever(sourcesDao.getActiveSources()).thenReturn(sourcesSubject.toFlowable(BackpressureStrategy.DROP))
        return sourcesSubject
    }

    private fun givenAllSourcesIsEmpty() {
        whenever(sourcesDao.getAll()).thenReturn(Flowable.just(emptyList()))
    }

    private companion object {

        const val GENERIC_ERR_MSG = "Something went wrong in refresh subscription"

        val SOURCE_1 = com.lelloman.simplerss.persistence.db.model.Source(
            id = 1,
            name = "source 1",
            url = "url 1",
            isActive = true
        )

        val SOURCE_2 = com.lelloman.simplerss.persistence.db.model.Source(
            id = 2,
            name = "source 2",
            url = "url 2",
            isActive = true
        )
    }
}