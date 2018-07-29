package com.lelloman.read.feed.finder

import com.lelloman.read.feed.FeedFetcher
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Maybe
import io.reactivex.Single
import org.junit.Test

class FeedFinderTest {

    private val httpClient: FeedFinderHttpClient = mock()
    private val feedFetcher: FeedFetcher = mock()
    private val parser: FeedFinderParser = mock()

    private val tested = FeedFinder(
        httpClient = httpClient,
        feedFetcher = feedFetcher,
        parser = parser
    )

    @Test
    fun `finds candidates urls`() {
        val baseUrl = "http://www.staceppa.com"
        val stringBody = ">_<"
        givenStringBodyAndBaseUrl(
            baseUrl = baseUrl,
            stringBody = stringBody
        )

        tested.findValidFeedUrls("").test()

        verify(parser).findCandidateUrls(baseUrl, stringBody)
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

    private fun givenStringBodyAndBaseUrl(baseUrl: String, stringBody: String) {
        whenever(httpClient.requestStringBodyAndBaseUrl(any())).thenReturn(
            Maybe.just(
                BaseUrlAndStringBody(
                    baseUrl = baseUrl,
                    stringBody = stringBody
                )
            )
        )
    }

    private fun givenHttpError() {
        whenever(httpClient.requestStringBodyAndBaseUrl(any())).thenReturn(Maybe.error(Exception()))
    }

    private fun givenUrlTestSuccessful() {
        whenever(feedFetcher.testUrl(any())).thenReturn(Single.just(FeedFetcher.TestResult.SUCCESS))
    }
}