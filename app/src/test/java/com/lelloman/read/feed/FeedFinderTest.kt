package com.lelloman.read.feed

import com.lelloman.read.core.HtmlParser
import com.lelloman.read.http.HttpClient
import com.lelloman.read.http.HttpResponse
import com.lelloman.read.utils.UrlValidator
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.junit.Test

class FeedFinderTest {

    private val httpClient: HttpClient = mock()
    private val urlValidator: UrlValidator = UrlValidator()
    private val htmlParser: HtmlParser = mock()

    private val tested = FeedFinder(
        httpClient = httpClient,
        urlValidator = urlValidator,
        htmlParser = htmlParser
    )

    @Test
    fun `finds base url with feed path`() {
        givenHttpResponse()
        val url = "http://www.staceppa.com/asdasd"

        val tester = tested.findValidFeedUrls(url).test()

        tester.assertValue { it == "http://www.staceppa.com/feed" }
    }

    private fun givenHttpResponse(httpResponse: HttpResponse = HttpResponse(
        code = 200,
        body = "<html></html>".toByteArray(),
        isSuccessful = true
    )) {
        whenever(httpClient.request(any())).thenReturn(Single.just(httpResponse))
    }
}