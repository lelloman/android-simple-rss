package com.lelloman.read.feed.finder

import com.lelloman.read.feed.fetcher.FeedFetcher
import com.lelloman.read.html.Doc
import com.lelloman.read.testutils.MockLoggerFactory
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers.trampoline
import io.reactivex.subjects.PublishSubject
import org.junit.Test

class FeedFinderTest {

    private val httpClient: FeedFinderHttpClient = mock()
    private val feedFetcher: FeedFetcher = mock()
    private val parser: FeedFinderParser = mock()
    private val loggerFactory = MockLoggerFactory()

    private val tested = FeedFinder(
        httpClient = httpClient,
        feedFetcher = feedFetcher,
        parser = parser,
        loggerFactory = loggerFactory,
        newThreadScheduler = trampoline()
    )

    @Test
    fun `finds candidates urls in first order url`() {
        val baseUrl = "http://www.staceppa.com"
        val doc = Doc()
        givenStringBodyAndBaseUrl(
            baseUrl = baseUrl,
            stringBody = ""
        )
        whenever(parser.parseDoc(any(), any())).thenReturn(Maybe.just(doc))
        whenever(parser.findCandidateUrls(any())).thenReturn(Observable.empty())

        tested.findValidFeedUrls("").test()

        verify(parser).findCandidateUrls(doc)
    }

    @Test
    fun `finds candidates urls in second order urls`() {
        val baseUrl = "http://www.staceppa.com"
        val doc = Doc()
        givenStringBodyAndBaseUrl(
            baseUrl = baseUrl,
            stringBody = ""
        )
        whenever(parser.parseDoc(baseUrl, "")).thenReturn(Maybe.just(doc))
        whenever(parser.findCandidateUrls(any())).thenReturn(Observable.empty())

        tested.findValidFeedUrls("").test()

        verify(parser).findCandidateUrls(doc)
    }

    @Test
    fun `silently drops http errors`() {
        givenHttpError()
        val url = "http://www.staceppa.com/asdasd"

        val tester = tested.findValidFeedUrls(url).test()

        tester.assertValueCount(0)
        tester.assertNoErrors()
        tester.assertComplete()
    }

    @Test
    fun `fires loading events`() {
        givenStringBodyAndBaseUrl("", "")
        whenever(parser.parseDoc("", "")).thenReturn(Maybe.just(Doc()))
        val candidatesSubject = PublishSubject.create<String>()
        whenever(parser.findCandidateUrls(any())).thenReturn(candidatesSubject.hide())

        val tester = tested.loading.test()
        tester.assertValues(false)

        tested.findValidFeedUrls("").test()
        tester.assertValues(false, true)

        candidatesSubject.onComplete()
        tester.assertValues(false, true, false)
    }

    private fun givenStringBodyAndBaseUrl(baseUrl: String, stringBody: String) {
        whenever(httpClient.requestStringBodyAndBaseUrl(any())).thenReturn(
            Maybe.just(
                StringBodyAndUrl(
                    url = baseUrl,
                    stringBody = stringBody
                )
            )
        )
    }

    private fun givenHttpError() {
        whenever(httpClient.requestStringBodyAndBaseUrl(any())).thenReturn(Maybe.error(Exception()))
    }
}