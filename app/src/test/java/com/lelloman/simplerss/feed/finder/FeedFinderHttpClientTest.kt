package com.lelloman.simplerss.feed.finder

import com.lelloman.common.utils.UrlValidator
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argThat
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Maybe
import io.reactivex.Single
import org.junit.Test

class FeedFinderHttpClientTest {

    private val httpClient: com.lelloman.simplerss.http.HttpClient = mock()
    private val urlValidator: UrlValidator = mock { _ ->
        on { maybePrependProtocol(any()) }.thenAnswer { it.arguments[0] as String }
        on { findBaseUrlWithProtocol(com.lelloman.simplerss.feed.finder.FeedFinderHttpClientTest.Companion.URL_WITH_PATH) }.thenReturn(Maybe.just(com.lelloman.simplerss.feed.finder.FeedFinderHttpClientTest.Companion.BASE_URL))
    }

    private val tested = com.lelloman.simplerss.feed.finder.FeedFinderHttpClient(
        httpClient = httpClient,
        urlValidator = urlValidator
    )

    @Test
    fun `requests string body`() {
        whenever(httpClient.request(any())).thenReturn(Single.just(com.lelloman.simplerss.http.HttpResponse(200, true, byteArrayOf())))

        tested.requestStringBody(com.lelloman.simplerss.feed.finder.FeedFinderHttpClientTest.Companion.BASE_URL).test()

        verify(urlValidator).maybePrependProtocol(com.lelloman.simplerss.feed.finder.FeedFinderHttpClientTest.Companion.BASE_URL)
        verify(httpClient).request(argThat { this.url == url })
    }

    @Test
    fun `re-throws exception from http client when requesting string body`() {
        val exception = com.lelloman.simplerss.http.HttpClientException(Exception("asdasd"))
        whenever(httpClient.request(any())).thenReturn(Single.error(exception))

        val tester = tested.requestStringBody(com.lelloman.simplerss.feed.finder.FeedFinderHttpClientTest.Companion.BASE_URL).test()

        tester.assertError { it == exception }
    }

    @Test
    fun `filters out unsuccessful http response when requesting string body`() {
        whenever(httpClient.request(any())).thenReturn(Single.just(
            com.lelloman.simplerss.http.HttpResponse(401, false, byteArrayOf())
        ))

        val tester = tested.requestStringBody(com.lelloman.simplerss.feed.finder.FeedFinderHttpClientTest.Companion.BASE_URL).test()

        tester.assertComplete()
        tester.assertValueCount(0)
    }

    @Test
    fun `requests string body and base url`() {
        val stringBody = "asdasd@"
        whenever(httpClient.request(any())).thenReturn(Single.just(com.lelloman.simplerss.http.HttpResponse(200, true, stringBody.toByteArray())))

        val tester = tested.requestStringBodyAndBaseUrl(com.lelloman.simplerss.feed.finder.FeedFinderHttpClientTest.Companion.URL_WITH_PATH).test()

        verify(urlValidator).findBaseUrlWithProtocol(com.lelloman.simplerss.feed.finder.FeedFinderHttpClientTest.Companion.URL_WITH_PATH)
        verify(httpClient).request(argThat {
            this.url == com.lelloman.simplerss.feed.finder.FeedFinderHttpClientTest.Companion.BASE_URL
        })
        tester.assertValueCount(1)
        tester.assertComplete()
        tester.assertValue { it.stringBody == stringBody && it.url == com.lelloman.simplerss.feed.finder.FeedFinderHttpClientTest.Companion.BASE_URL }
    }

    @Test
    fun `drops http exceptions when requesting string body and base url`() {
        whenever(httpClient.request(any())).thenReturn(Single.error(Exception()))

        val tester = tested.requestStringBodyAndBaseUrl(com.lelloman.simplerss.feed.finder.FeedFinderHttpClientTest.Companion.URL_WITH_PATH).test()

        tester.assertComplete()
        tester.assertValueCount(0)
    }

    @Test
    fun `filters unsuccessful http response when requesting string body and base url`() {
        whenever(httpClient.request(any())).thenReturn(Single.just(com.lelloman.simplerss.http.HttpResponse(
            code = 401,
            isSuccessful = false
        )))

        val tester = tested.requestStringBodyAndBaseUrl(com.lelloman.simplerss.feed.finder.FeedFinderHttpClientTest.Companion.URL_WITH_PATH).test()

        verify(httpClient).request(argThat { this.url == com.lelloman.simplerss.feed.finder.FeedFinderHttpClientTest.Companion.BASE_URL })
        tester.assertComplete()
        tester.assertValueCount(0)
    }

    @Test
    fun `filters http response with empty body when requesting string body and base url`() {
        whenever(httpClient.request(any())).thenReturn(Single.just(com.lelloman.simplerss.http.HttpResponse(
            code = 200,
            isSuccessful = true,
            body = byteArrayOf()
        )))

        val tester = tested.requestStringBodyAndBaseUrl(com.lelloman.simplerss.feed.finder.FeedFinderHttpClientTest.Companion.URL_WITH_PATH).test()

        verify(httpClient).request(argThat { this.url == com.lelloman.simplerss.feed.finder.FeedFinderHttpClientTest.Companion.BASE_URL })
        tester.assertComplete()
        tester.assertValueCount(0)
    }

    private companion object {
        const val BASE_URL = "http://www.staceppa.com"
        const val URL_WITH_PATH = "${com.lelloman.simplerss.feed.finder.FeedFinderHttpClientTest.Companion.BASE_URL}/asdasd"
    }
}