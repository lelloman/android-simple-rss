package com.lelloman.read.feed

import com.lelloman.read.http.HttpClient
import com.lelloman.read.persistence.ArticlesDao
import com.lelloman.read.persistence.SourcesDao
import com.lelloman.read.persistence.model.Source
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.junit.Test

class FeedManagerImplTest {

    private val httpClient: HttpClient = mock()
    private val feedParser: FeedParser = mock()
    private val sourcesDao: SourcesDao = mock()
    private val articlesDao: ArticlesDao = mock()

    private val dependencies = arrayOf(httpClient, feedParser, sourcesDao, articlesDao)

    private val tested = FeedManagerImpl(
        httpClient = httpClient,
        feedParser = feedParser,
        sourcesDao = sourcesDao,
        articlesDao = articlesDao
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

    private fun givenSourcesSubject(): Subject<List<Source>> {
        val sourcesSubject = PublishSubject.create<List<Source>>()
        whenever(sourcesDao.getActiveSources()).thenReturn(sourcesSubject.toFlowable(BackpressureStrategy.DROP))
        return sourcesSubject
    }

}