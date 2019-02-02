package com.lelloman.read.ui.common

import com.lelloman.common.logger.Logger
import com.lelloman.read.feed.finder.FeedFinder
import com.lelloman.read.feed.finder.FoundFeed
import com.lelloman.read.persistence.db.SourcesDao
import com.lelloman.read.persistence.db.model.Source
import com.lelloman.read.testutils.dummyFoundFeed
import com.lelloman.read.ui.common.repository.DiscoverRepository
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import io.reactivex.BackpressureStrategy
import io.reactivex.schedulers.Schedulers.trampoline
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.junit.Test

class DiscoverRepositoryTest {

    private val feedFinderFindUrlsSubject: Subject<FoundFeed> = PublishSubject.create()
    private val feedFinder: FeedFinder = mock {
        on { findValidFeedUrls(any()) }.thenReturn(feedFinderFindUrlsSubject.hide())
    }

    private val allSourcesSubject = PublishSubject.create<List<Source>>()
    private val sourcesDao: SourcesDao = mock {
        on { getAll() }.thenReturn(allSourcesSubject.toFlowable(BackpressureStrategy.LATEST))
    }

    private val logger: Logger = mock()

    private val tested = DiscoverRepository(
        ioScheduler = trampoline(),
        feedFinder = feedFinder,
        sourcesDao = sourcesDao,
        loggerFactory = mock {
            on { getLogger(any()) }.thenReturn(logger)
        }
    )

    @Test
    fun `upon instantiation is finding feeds emits false on subscription`() {
        val tester1 = tested.isFindingFeeds.test()
        val tester2 = tested.isFindingFeeds.test()

        tester1.assertValues(false)
        tester2.assertValues(false)
    }

    @Test
    fun `is finding feeds emits true after find feeds is called`() {
        val tester = tested.isFindingFeeds.test()
        tester.assertValues(false)

        tested.findFeeds("asd")

        tester.assertValues(false, true)
    }

    @Test
    fun `is finding feeds emits false when feeds finding is finishes`() {
        val tester = tested.isFindingFeeds.test()

        tested.findFeeds("asd")
        feedFinderFindUrlsSubject.onComplete()

        tester.assertValues(false, true, false)
    }

    @Test
    fun `emits valid feed urls`() {
        val tester = tested.foundFeeds.test()
        tester.assertNoValues()

        tested.findFeeds("asd")
        allSourcesSubject.onNext(emptyList())
        tester.assertValueCount(1)
        tester.assertValueAt(0) { it.isEmpty() }

        feedFinderFindUrlsSubject.onNext(dummyFoundFeed())
        tester.assertValueCount(2)
        tester.assertValueAt(1) { it.size == 1 }

        feedFinderFindUrlsSubject.onNext(dummyFoundFeed())
        tester.assertValueCount(3)
        tester.assertValueAt(2) { it.size == 2 }
    }

    @Test
    fun `does nothing if find feeds is called a second time with same url`() {
        val tester = tested.foundFeeds.test()
        tester.assertValueCount(0)
        val url = "www.staceppa.it"

        tested.findFeeds(url)
        tester.assertValueCount(1)
        tester.assertValueAt(0) { it.isEmpty() }

        tested.findFeeds(url)
        tester.assertValueCount(1)
    }

    @Test
    fun `logs a warning and does nothing if find url is called while already finding`() {
        val url1 = "www.staceppa.it"
        val url2 = "www.staceppa.com"
        val tester = tested.foundFeeds.test()
        tester.assertValueCount(0)

        tested.findFeeds(url1)
        tester.assertValueCount(1)
        tester.assertValueAt(0) { it.isEmpty() }
        verify(logger, never()).w(any(), any())
        reset(feedFinder)

        tested.findFeeds(url2)
        tester.assertValueCount(1)
        verify(logger).w(any(), eq(null))
        verifyZeroInteractions(feedFinder)
    }

    @Test
    fun `restarts feed finding when find feeds is called a second time with same url but previous finding was finished`() {
        val tester = tested.foundFeeds.test()
        tester.assertValueCount(0)
        val url = "www.staceppa.it"

        tested.findFeeds(url)
        tester.assertValueCount(1)
        tester.assertValueAt(0) { it.isEmpty() }
        feedFinderFindUrlsSubject.onComplete()

        tested.findFeeds(url)
        tester.assertValueCount(2)
        tester.assertValueAt(1) { it.isEmpty() }
    }
}