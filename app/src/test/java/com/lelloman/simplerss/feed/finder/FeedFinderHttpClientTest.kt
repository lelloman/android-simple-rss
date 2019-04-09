package com.lelloman.simplerss.feed.finder

import com.lelloman.common.http.HttpClient
import com.lelloman.common.http.HttpClientException
import com.lelloman.common.http.HttpResponse
import com.lelloman.common.utils.UrlValidator
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Maybe
import io.reactivex.Single
import org.junit.Test

class FeedFinderHttpClientTest {

    private val httpClient: HttpClient = mock()
    private val urlValidator: UrlValidator = mock { _ ->
        on { maybePrependProtocol(any()) }.thenAnswer { it.arguments[0] as String }
        on { findBaseUrlWithProtocol(URL_WITH_PATH) }.thenReturn(Maybe.just(BASE_URL))
    }

    private val tested = FeedFinderHttpClient(
        httpClient = httpClient,
        urlValidator = urlValidator
    )

    @Test
    fun `requests string body`() {
        whenever(httpClient.request(any())).thenReturn(Single.just(HttpResponse(200, true, byteArrayOf())))

        tested.requestStringBody(BASE_URL).test()

        verify(urlValidator).maybePrependProtocol(BASE_URL)
        verify(httpClient).request(argThat { this.url == url })
    }

    @Test
    fun `re-throws exception from http client when requesting string body`() {
        val exception = HttpClientException(Exception("asdasd"))
        whenever(httpClient.request(any())).thenReturn(Single.error(exception))

        val tester = tested.requestStringBody(BASE_URL).test()

        tester.assertError { it == exception }
    }

    @Test
    fun `filters out unsuccessful http response when requesting string body`() {
        whenever(httpClient.request(any())).thenReturn(Single.just(
            HttpResponse(401, false, byteArrayOf())
        ))

        val tester = tested.requestStringBody(BASE_URL).test()

        tester.assertComplete()
        tester.assertValueCount(0)
    }

    @Test
    fun `requests string body and base url`() {
        val stringBody = "asdasd@"
        whenever(httpClient.request(any())).thenReturn(Single.just(HttpResponse(200, true, stringBody.toByteArray())))

        val tester = tested.requestStringBodyAndBaseUrl(URL_WITH_PATH).test()

        verify(urlValidator).findBaseUrlWithProtocol(URL_WITH_PATH)
        verify(httpClient).request(argThat {
            this.url == BASE_URL
        })
        tester.assertValueCount(1)
        tester.assertComplete()
        tester.assertValue { it.stringBody == stringBody && it.url == BASE_URL }
    }

    @Test
    fun `drops http exceptions when requesting string body and base url`() {
        whenever(httpClient.request(any())).thenReturn(Single.error(Exception()))

        val tester = tested.requestStringBodyAndBaseUrl(URL_WITH_PATH).test()

        tester.assertComplete()
        tester.assertValueCount(0)
    }

    @Test
    fun `filters unsuccessful http response when requesting string body and base url`() {
        whenever(httpClient.request(any())).thenReturn(Single.just(HttpResponse(
            code = 401,
            isSuccessful = false
        )))

        val tester = tested.requestStringBodyAndBaseUrl(URL_WITH_PATH).test()

        verify(httpClient).request(argThat { this.url == BASE_URL })
        tester.assertComplete()
        tester.assertValueCount(0)
    }

    @Test
    fun `filters http response with empty body when requesting string body and base url`() {
        whenever(httpClient.request(any())).thenReturn(Single.just(HttpResponse(
            code = 200,
            isSuccessful = true,
            body = byteArrayOf()
        )))

        val tester = tested.requestStringBodyAndBaseUrl(URL_WITH_PATH).test()

        verify(httpClient).request(argThat { this.url == BASE_URL })
        tester.assertComplete()
        tester.assertValueCount(0)
    }

    private companion object {
        const val BASE_URL = "http://www.staceppa.com"
        const val URL_WITH_PATH = "$BASE_URL/asdasd"
    }
}