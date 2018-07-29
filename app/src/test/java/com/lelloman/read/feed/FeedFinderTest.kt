package com.lelloman.read.feed

import com.lelloman.read.html.Doc
import com.lelloman.read.html.HtmlParser
import com.lelloman.read.http.HttpClient
import com.lelloman.read.http.HttpResponse
import com.lelloman.read.utils.UrlValidator
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Maybe
import io.reactivex.Single
import org.junit.Test

class FeedFinderTest {

    private val httpClient: HttpClient = mock()
    private val urlValidator: UrlValidator = mock()
    private val htmlParser: HtmlParser = mock()
    private val feedFetcher: FeedFetcher = mock()

    private val tested = FeedFinder(
        httpClient = httpClient,
        urlValidator = urlValidator,
        htmlParser = htmlParser,
        feedFetcher = feedFetcher
    )

    @Test
    fun `finds base url with feed path`() {
        givenBaseUrl("http://www.staceppa.com")
        givenHttpResponse()
        givenHtmlParsesEmptyDoc()
        givenUrlTestSuccessful()
        val url = "http://www.staceppa.com/asdasd"

        val tester = tested.findValidFeedUrls(url).test()

        tester.assertValue { it == "http://www.staceppa.com/feed" }
    }

    @Test
    fun `silently drops http errors`() {
        givenBaseUrl("http://www.staceppa.com")
        givenHttpError()
        val url = "http://www.staceppa.com/asdasd"

        val tester = tested.findValidFeedUrls(url).test()

        tester.assertValueCount(0)
        tester.assertNoErrors()
        tester.assertComplete()
    }

    private fun givenBaseUrl(baseUrl: String) {
        whenever(urlValidator.findBaseUrlWithProtocol(any())).thenReturn(Maybe.just(baseUrl))
    }

    private fun givenHttpError() {
        whenever(httpClient.request(any())).thenReturn(Single.error(Exception()))
    }

    private fun givenUrlTestSuccessful() {
        whenever(feedFetcher.testUrl(any())).thenReturn(Single.just(FeedFetcher.TestResult.SUCCESS))
    }

    private fun givenHtmlParsesEmptyDoc() {
        whenever(htmlParser.parseDoc(any())).thenReturn(Doc())
    }

    private fun givenHttpResponse(httpResponse: HttpResponse = HttpResponse(
        code = 200,
        body = "<html></html>".toByteArray(),
        isSuccessful = true
    )) {
        whenever(httpClient.request(any())).thenReturn(Single.just(httpResponse))
    }
}