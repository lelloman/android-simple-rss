package com.lelloman.simplerss.feed

import com.lelloman.common.http.HttpClient
import com.lelloman.common.http.HttpResponse
import com.lelloman.common.http.request.HttpRequest
import com.lelloman.common.jvmtestutils.MockLoggerFactory
import com.lelloman.common.utils.UrlValidatorImpl
import com.lelloman.simplerss.feed.fetcher.BitmapDecoder
import com.lelloman.simplerss.feed.fetcher.FaviconFetcher
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Test

class FaviconFetcherTest {

    private val httpClient: HttpClient = mock()
    private val bitmapDecoder: BitmapDecoder = mock { on { decodeBitmap(any()) }.thenReturn(mock()) }
    private val urlValidator = UrlValidatorImpl()

    private val tested = FaviconFetcher(
        httpClient = httpClient,
        urlValidator = urlValidator,
        loggerFactory = MockLoggerFactory(),
        bitmapDecoder = bitmapDecoder
    )

    @Test
    fun `makes http request to duck duck go favicon service with base url`() {
        val url = "http://www.staceppa.com/asdasd"
        val faviconBytes = byteArrayOf(1, 2, 3)
        val httpResponse = HttpResponse(200, true, body = faviconBytes)
        whenever(httpClient.request(argThat { this.url.contains("duckduckgo") }))
            .thenReturn(Single.just(httpResponse))

        val tester = tested.getPngFavicon(url).test()

        verify(httpClient).request(HttpRequest(tested.getDuckDuckGoFaviconUrl("www.staceppa.com")))
        verifyZeroInteractions(httpClient)
        tester.assertValue(faviconBytes)
    }

    @Test
    fun `makes http request to google s2 if request to duckduckgo fails`() {
        val url = "http://www.staceppa.com/asdasd"
        val faviconBytes = byteArrayOf(1, 2, 3)
        val httpResponse = HttpResponse(200, true, body = faviconBytes)
        whenever(httpClient.request(argThat { this.url.contains("duckduckgo") }))
            .thenReturn(Single.just(HttpResponse(500, false)))
        whenever(httpClient.request(argThat { this.url.contains("google") }))
            .thenReturn(Single.just(httpResponse))

        val tester = tested.getPngFavicon(url).test()

        verify(httpClient).request(HttpRequest(tested.getDuckDuckGoFaviconUrl("www.staceppa.com")))
        verify(httpClient).request(HttpRequest(tested.getGoogleS2FaviconUrl("www.staceppa.com")))
        tester.assertValue(faviconBytes)
    }

    @Test
    fun `swallows http exception`() {
        whenever(httpClient.request(any())).thenReturn(Single.error(Exception()))

        val tester = tested.getPngFavicon("http://www.staceppa.com").test()

        tester.assertValues()
        tester.assertComplete()
    }

    @Test
    fun `completes with no event if http response is unsuccessul`() {
        val httpResponse = HttpResponse(
            code = 500,
            isSuccessful = false,
            body = ByteArray(0)
        )
        whenever(httpClient.request(any())).thenReturn(Single.just(httpResponse))

        val tester = tested.getPngFavicon("http://www.staceppa.com").test()

        tester.assertValues()
        tester.assertComplete()
    }

    @Test
    fun `returns byte content of http response`() {
        val body = byteArrayOf(1, 2, 3, 4)
        val httpResponse = HttpResponse(
            code = 200,
            isSuccessful = true,
            body = body
        )
        whenever(httpClient.request(any())).thenReturn(Single.just(httpResponse))

        val tester = tested.getPngFavicon("http://www.staceppa.com").test()

        tester.assertValues(body)
        tester.assertComplete()
    }
}