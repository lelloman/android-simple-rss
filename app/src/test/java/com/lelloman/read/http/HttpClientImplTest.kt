package com.lelloman.read.http

import com.lelloman.read.testutils.MockLoggerFactory
import com.lelloman.read.testutils.MockTimeProvider
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argThat
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import okhttp3.Call
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import org.junit.Test

class HttpClientImplTest {

    private val okHttpResponse: Response = mock()
    private val okHttpCall: Call = mock {
        on { execute() }.thenAnswer { _ -> okHttpResponse }
    }

    private val okHttpClient: OkHttpClient = mock {
        on { newCall(any()) }.thenAnswer { _ -> okHttpCall }
    }
    private val loggerFactory = MockLoggerFactory()
    private val timeProvider = MockTimeProvider()

    private val tested = HttpClientImpl(
        okHttpClient = okHttpClient,
        loggerFactory = loggerFactory,
        timeProvider = timeProvider
    )

    @Test
    fun `executes ok http GET call`() {
        val tester = tested.request(HTTP_REQUEST).test()

        tester.assertNoErrors()
        verify(okHttpClient).newCall(argThat {
            this.method() == "GET" && this.url() == HttpUrl.parse(HTTP_REQUEST.url)
        })
    }

    @Test
    fun `returns http status code in response`() {
        val responseCode = 0xb00b5
        whenever(okHttpResponse.code()).thenReturn(responseCode)

        val tester = tested.request(HTTP_REQUEST).test()

        tester.assertNoErrors()
        tester.assertValue { it.code == responseCode }
    }

    @Test
    fun `returns successful property in response`() {
        whenever(okHttpResponse.isSuccessful).thenReturn(true)

        val tester = tested.request(HTTP_REQUEST).test()

        tester.assertNoErrors()
        tester.assertValue { it.isSuccessful }
    }

    @Test
    fun `returns unsuccessful property in response`() {
        whenever(okHttpResponse.isSuccessful).thenReturn(false)

        val tester = tested.request(HTTP_REQUEST).test()

        tester.assertNoErrors()
        tester.assertValue { !it.isSuccessful }
    }

    @Test
    fun `returns empty string body if ok http response body is null`() {
        whenever(okHttpResponse.body()).thenReturn(null)

        val tester = tested.request(HTTP_REQUEST).test()

        tester.assertNoErrors()
        tester.assertValue { it.stringBody == "" }
    }

    @Test
    fun `returns empty string body if string from ok http response body is null`() {
        val responseBody = ResponseBody.create(null, ByteArray(0))
        whenever(okHttpResponse.body()).thenReturn(responseBody)

        val tester = tested.request(HTTP_REQUEST).test()

        tester.assertNoErrors()
        tester.assertValue { it.stringBody == "" }
    }

    @Test
    fun `returns string body from ok http response`() {
        val body = "bo bo body"
        val responseBody = ResponseBody.create(null, body.toByteArray())
        whenever(okHttpResponse.body()).thenReturn(responseBody)

        val tester = tested.request(HTTP_REQUEST).test()

        tester.assertNoErrors()
        tester.assertValue { it.stringBody == body }
    }

    @Test
    fun `throws HttpClientException if ok http call throws exception`() {
        val exception = IllegalAccessException("smurfs")
        whenever(okHttpClient.newCall(any())).thenAnswer { throw exception }

        val tester = tested.request(HTTP_REQUEST).test()

        tester.assertError { it is HttpClientException && it.cause == exception }
    }

    @Test
    fun `throws HttpClientException if ok http call execution throws exception`() {
        val exception = IllegalAccessException("smurfs")
        whenever(okHttpCall.execute()).thenAnswer { throw exception }

        val tester = tested.request(HTTP_REQUEST).test()

        tester.assertError { it is HttpClientException && it.cause == exception }
    }

    private companion object {
        val HTTP_REQUEST = HttpRequest("http://www.staceppa.com")
    }
}