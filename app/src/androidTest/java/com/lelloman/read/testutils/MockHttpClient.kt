package com.lelloman.read.testutils

import android.support.test.InstrumentationRegistry
import okhttp3.Call
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import org.mockito.Mockito

class MockHttpClient : OkHttpClient() {

    private val fileResponses = mapOf(
        URL_FANPAGE_FEED to "rss_fanpage.xml",
        URL_REPUBBLICA_FEED to "rss_repubblica.xml",
        URL_ASD_FEED to "rss_repubblica.xml"
    )

    private val literalResponses = mapOf(
        "http://www.asd.com" to """
                            <html>
                            <head></head>
                            <body>
                            <a href="$URL_REPUBBLICA_FEED">feed rss 1</a>
                            <a href="$URL_FANPAGE_FEED">feed rss 2</a>
                            </body>
                            </html>
                        """.trimIndent()
    )

    private fun successfulResponse(body: ByteArray) = Response
        .Builder()
        .code(200)
        .body(ResponseBody.create(MediaType.get("text/xml"), body))

    override fun newCall(request: Request): Call {
        val url = request
            .url()
            .url()
            .toExternalForm()
            .removeSuffix("/")
        val responseBuilder = when {
            fileResponses.containsKey(url) -> {
                val body = InstrumentationRegistry
                    .getContext()
                    .assets
                    .open(fileResponses[url])
                    .bufferedReader()
                    .readText()
                    .toByteArray()
                successfulResponse(body)
            }
            literalResponses.containsKey(url) -> {
                successfulResponse(literalResponses[url]!!.toByteArray())
            }
            else -> Response
                .Builder()
                .code(404)
        }
        val call = Mockito.mock(Call::class.java)
        val response = responseBuilder
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .message("hello")
            .build()
        whenever(call.execute()).thenReturn(response)
        return call
    }

    companion object {
        const val URL_FANPAGE_FEED = "http://www.fanpage.it/feed"
        const val FANPAGE_ARTICLES_COUNT = 23
        const val URL_REPUBBLICA_FEED = "http://www.repubblica.it/feed"
        const val REPUBBLICA_ARTICLES_COUNT = 118
        const val URL_ASD = "http://www.asd.com"
        const val URL_ASD_FEED = "http://www.asd.com/feed"
    }
}