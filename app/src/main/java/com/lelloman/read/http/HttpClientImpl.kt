package com.lelloman.read.http

import com.lelloman.common.utils.TimeProvider
import com.lelloman.read.core.logger.LoggerFactory
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.Request

class HttpClientImpl(
    private val okHttpClient: OkHttpClient,
    loggerFactory: LoggerFactory,
    private val timeProvider: TimeProvider
) : HttpClient {

    private val logger = loggerFactory.getLogger(javaClass.simpleName)

    override fun request(request: HttpRequest): Single<HttpResponse> = Single.fromCallable {
        try {
            val okRequest = Request.Builder()
                .url(request.url)
                .build()

            logger.d("--> ${okRequest.method()} ${okRequest.url()} ${okRequest.headers()}")

            val t1 = timeProvider.nowUtcMs()
            val okResponse = okHttpClient.newCall(okRequest).execute()
            val t2 = timeProvider.nowUtcMs()
            val body = okResponse.body()?.bytes() ?: ByteArray(0)
            logger.d("<-- ${okRequest.method()} ${okResponse.code()} ${okRequest.url()} in ${t2 - t1}ms ${okResponse.headers()}")
            logger.d("response length: ${body.size}")

            HttpResponse(
                code = okResponse.code(),
                isSuccessful = okResponse.isSuccessful,
                body = body
            )
        } catch (exception: Exception) {
            throw HttpClientException(exception)
        }
    }
}