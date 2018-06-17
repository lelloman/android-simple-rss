package com.lelloman.read.http

import android.util.Log
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.Request

class HttpClientImpl(
    private val okHttpClient: OkHttpClient
) : HttpClient {

    override fun request(request: HttpRequest): Single<HttpResponse> = Single.fromCallable {
        val okRequest = Request.Builder()
            .url(request.url)
            .build()

        Log.d("OkHttp", "--> ${okRequest.method()} ${okRequest.url()} ${okRequest.headers()}")

        val t1 = System.currentTimeMillis()
        val okResponse = okHttpClient.newCall(okRequest).execute()
        val t2 = System.currentTimeMillis()
        val body = okResponse.body()?.string() ?: ""
        Log.d("OkHttp", "<-- ${okRequest.method()} ${okResponse.code()} ${okRequest.url()} in ${t2 - t1}ms ${okResponse.headers()}")
        Log.d("OkHttp", "response length: ${body.length}")

        HttpResponse(
            code = okResponse.code(),
            isSuccessful = okResponse.isSuccessful,
            body = body
        )
    }
}