package com.lelloman.read.feed

import com.lelloman.read.http.HttpClient
import com.lelloman.read.http.HttpRequest
import com.lelloman.read.http.HttpResponse
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.junit.Test

class FaviconFetcherTest {

    private val httpClient: HttpClient = mock()

    private val tested = FaviconFetcher(httpClient)

    @Test
    fun `find base url 1`() {
        val url = "http://www.staceppa.com"

        val tester = tested.findBaseUrl(url).test()

        tester.assertValues("www.staceppa.com")
    }

    @Test
    fun `find base url 2`() {
        val url = "https://www.staceppa.com/asdasdasd/12345"

        val tester = tested.findBaseUrl(url).test()

        tester.assertValues("www.staceppa.com")
    }

    @Test
    fun `find base url 3`() {
        val url = "http://www.staceppa.com/-_-/-_-/"

        val tester = tested.findBaseUrl(url).test()

        tester.assertValues("www.staceppa.com")
    }

    @Test
    fun `does not find url with missing protocol`() {
        val url = "www.staceppa.com"

        val tester = tested.findBaseUrl(url).test()

        tester.assertValues()
        tester.assertComplete()
    }

    @Test
    fun `does not find url with weird protocol 1`() {
        val url = "theweirdprotocol://www.staceppa.com"

        val tester = tested.findBaseUrl(url).test()

        tester.assertValues()
        tester.assertComplete()
    }

    @Test
    fun `makes http to google s2 with base url`() {
        val url = "http://www.staceppa.com/asdasd"

        tested.getPngFavicon(url).test()

        verify(httpClient).request(HttpRequest(tested.getGoogleS2FaviconUrl("www.staceppa.com")))
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