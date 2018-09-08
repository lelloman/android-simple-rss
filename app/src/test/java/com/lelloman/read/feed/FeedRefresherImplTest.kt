package com.lelloman.read.feed

import com.lelloman.read.feed.fetcher.FaviconFetcher
import com.lelloman.read.feed.fetcher.FeedFetcher
import com.lelloman.read.mock.MockAppSettings
import com.lelloman.read.mock.MockLogger
import com.lelloman.read.mock.MockLoggerFactory
import com.lelloman.read.mock.MockTimeProvider
import com.lelloman.read.persistence.db.ArticlesDao
import com.lelloman.read.persistence.db.SourcesDao
import com.lelloman.read.persistence.db.model.Article
import com.lelloman.read.persistence.db.model.Source
import com.lelloman.read.persistence.settings.SourceRefreshInterval
import com.lelloman.read.testutils.dummySource
import com.lelloman.read.utils.Constants.AppSettings.DEFAULT_MIN_SOURCE_REFRESH_INTERVAL
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

    private val sourcesDao: SourcesDao = mock()
    private val articlesDao: ArticlesDao = mock()
    private val timeProvider = MockTimeProvider()
    private val appSettings = MockAppSettings()
    private val logger = MockLogger()
    private val loggerFactory = MockLoggerFactory(logger)
    private val feedFetcher: FeedFetcher = mock()
    private val faviconFetcher: FaviconFetcher = mock()

    private val dependencies = arrayOf(feedFetcher, sourcesDao, articlesDao)

    private val tested = FeedRefresherImpl(
        ioScheduler = trampoline(),
        newThreadScheduler = trampoline(),
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
        val minRefreshInterval = SourceRefreshInterval.NEUROTIC
        givenHasMinRefreshInterval(minRefreshInterval)
        whenever(feedFetcher.fetchFeed(any())).thenAnswer {
            Maybe.just(it.arguments[0] as Source to emptyList<Article>())
        }
        givenHasTime(now)
        val staleSource = Source(id = 1L,
            name = "stale source",
            url = "url 1",
            lastFetched = now - (minRefreshInterval.ms * 2),
            isActive = true
        )
        val updatedSource = Source(id = 2L,
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
        givenHasMinRefreshInterval(SourceRefreshInterval.NEUROTIC)
        givenHasActiveSources(SOURCE_1)

        tested.refresh()

        logger.assertNeverLoggedError()
    }

    @Test
    fun `logs error when fetching active sources`() {
        givenAllSourcesIsEmpty()
        givenHasDefaultMinRefreshInterval()
        val error = Exception("crasc")
        whenever(sourcesDao.getActiveSources()).thenReturn(Flowable.error(error))

        tested.refresh()

        logger.assertLoggedError(GENERIC_ERR_MSG, error)
    }

    @Test
    fun `updates db when fetched feed successfully`() {
        givenAllSourcesIsEmpty()
        givenHasActiveSources(SOURCE_1, SOURCE_2)
        givenHasTime(Long.MAX_VALUE)
        givenHasMinRefreshInterval(SourceRefreshInterval.NEUROTIC)
        val articles1 = listOf<Article>(mock())
        val articles2 = listOf<Article>(mock(), mock())
        whenever(feedFetcher.fetchFeed(SOURCE_1)).thenReturn(Maybe.just(SOURCE_1 to articles1))
        whenever(feedFetcher.fetchFeed(SOURCE_2)).thenReturn(Maybe.just(SOURCE_2 to articles2))

        tested.refresh()

        verify(articlesDao).deleteArticlesFromSource(SOURCE_1.id)
        verify(articlesDao).deleteArticlesFromSource(SOURCE_2.id)
        verify(articlesDao).insertAll(eq(articles1[0]))
        verify(articlesDao).insertAll(eq(articles2[0]), eq(articles2[1]))
        verify(sourcesDao).updateSourceLastFetched(SOURCE_1.id, timeProvider.nowUtcMs())
        verify(sourcesDao).updateSourceLastFetched(SOURCE_2.id, timeProvider.nowUtcMs())
    }

    @Test
    fun `downloads favicons on refresh`() {
        givenHasActiveSources()
        givenHasMinRefreshInterval(SourceRefreshInterval.NEUROTIC)
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

    private fun givenHasMinRefreshInterval(sourceRefreshInterval: SourceRefreshInterval) {
        appSettings.providedSourceRefreshMinInterval = Observable.just(sourceRefreshInterval)
    }

    private fun givenHasActiveSources(vararg source: Source) {
        whenever(sourcesDao.getActiveSources()).thenReturn(Flowable.just(source.toList()))
    }

    private fun givenHasTime(time: Long) {
        timeProvider.now = time
    }

    private fun givenActiveSourcesSubject(): Subject<List<Source>> {
        val sourcesSubject = PublishSubject.create<List<Source>>()
        whenever(sourcesDao.getActiveSources()).thenReturn(sourcesSubject.toFlowable(BackpressureStrategy.DROP))
        return sourcesSubject
    }

    private fun givenAllSourcesIsEmpty() {
        whenever(sourcesDao.getAll()).thenReturn(Flowable.just(emptyList()))
    }

    private companion object {

        const val GENERIC_ERR_MSG = "Something went wrong in refresh subscription"

        val SOURCE_1 = Source(
            id = 1,
            name = "source 1",
            url = "url 1",
            isActive = true
        )

        val SOURCE_2 = Source(
            id = 2,
            name = "source 2",
            url = "url 2",
            isActive = true
        )
    }
}