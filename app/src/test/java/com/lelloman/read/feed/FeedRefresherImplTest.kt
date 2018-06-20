package com.lelloman.read.feed

import com.lelloman.read.core.TimeProvider
import com.lelloman.read.core.logger.Logger
import com.lelloman.read.core.logger.LoggerFactory
import com.lelloman.read.persistence.db.ArticlesDao
import com.lelloman.read.persistence.db.SourcesDao
import com.lelloman.read.persistence.db.model.Article
import com.lelloman.read.persistence.db.model.Source
import com.lelloman.read.persistence.settings.AppSettings
import com.lelloman.read.persistence.settings.SourceRefreshInterval
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
import io.reactivex.schedulers.Schedulers.trampoline
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.junit.Test

class FeedRefresherImplTest {

    private val sourcesDao: SourcesDao = mock()
    private val articlesDao: ArticlesDao = mock()
    private val timeProvider: TimeProvider = mock()
    private val appSettings: AppSettings = mock()
    private val logger: Logger = mock()
    private val loggerFactory: LoggerFactory = mock {
        on { getLogger(any()) }.thenReturn(logger)
    }
    private val feedFetcher: FeedFetcher = mock()

    private val dependencies = arrayOf(feedFetcher, sourcesDao, articlesDao)

    private val tested = FeedRefresherImpl(
        ioScheduler = trampoline(),
        newThreadScheduler = trampoline(),
        sourcesDao = sourcesDao,
        articlesDao = articlesDao,
        timeProvider = timeProvider,
        appSettings = appSettings,
        loggerFactory = loggerFactory,
        feedFetcher = feedFetcher
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
        givenSourcesSubject()
        val tester = tested.isLoading.test()

        tested.refresh()

        tester.assertValues(false, true)
    }

    @Test
    fun `does nothing if refresh is called while loading`() {
        givenSourcesSubject()
        val tester = tested.isLoading.test()
        tested.refresh()
        reset(*dependencies)

        tested.refresh()

        verifyZeroInteractions(*dependencies)
        tester.assertValues(false, true)
    }

    @Test
    fun `stops loading when finish refresh successfully`() {
        val sourcesSubject = givenSourcesSubject()
        val tester = tested.isLoading.test()

        tested.refresh()
        sourcesSubject.onNext(emptyList())

        tester.awaitCount(3)
        tester.assertValues(false, true, false)
    }

    @Test
    fun `stops loading when finish refresh with error`() {
        val sourcesSubject = givenSourcesSubject()
        val tester = tested.isLoading.test()

        tested.refresh()
        sourcesSubject.onError(Exception("meow"))

        tester.assertValues(false, true, false)
    }

    @Test
    fun `refreshes stale sources only`() {
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
        whenever(feedFetcher.fetchFeed(any())).thenReturn(Maybe.error(Exception()))
        givenHasTime(Long.MAX_VALUE)
        givenHasMinRefreshInterval(SourceRefreshInterval.NEUROTIC)
        givenHasActiveSources(SOURCE_1)

        tested.refresh()

        verify(logger, never()).e(any(), any())
    }

    @Test
    fun `logs error when fetching active sources`() {
        val error = Exception("crasc")
        whenever(sourcesDao.getActiveSources()).thenReturn(Flowable.error(error))

        tested.refresh()

        verify(logger).e(GENERIC_ERR_MSG, error)
    }

    @Test
    fun `updates db when fetched feed successfully`() {
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

    private fun givenHasMinRefreshInterval(sourceRefreshInterval: SourceRefreshInterval) {
        whenever(appSettings.sourceRefreshMinInterval).thenReturn(sourceRefreshInterval)
    }

    private fun givenHasActiveSources(vararg source: Source) {
        whenever(sourcesDao.getActiveSources()).thenReturn(Flowable.just(source.toList()))
    }

    private fun givenHasTime(time: Long) {
        whenever(timeProvider.nowUtcMs()).thenReturn(time)
    }

    private fun givenSourcesSubject(): Subject<List<Source>> {
        val sourcesSubject = PublishSubject.create<List<Source>>()
        whenever(sourcesDao.getActiveSources()).thenReturn(sourcesSubject.toFlowable(BackpressureStrategy.DROP))
        return sourcesSubject
    }

    private companion object {

        val GENERIC_ERR_MSG = "Something went wrong in refresh subscription"

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