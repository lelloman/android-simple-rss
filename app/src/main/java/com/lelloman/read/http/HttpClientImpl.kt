package com.lelloman.read.http

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

        val okResponse = okHttpClient.newCall(okRequest).execute()
        val body = okResponse.body()?.string() ?: ""

        HttpResponse(
            code = okResponse.code(),
            isSuccessful = okResponse.isSuccessful,
            body = body
        )
    }
}