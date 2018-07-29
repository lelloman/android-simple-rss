package com.lelloman.read.feed.finder

import com.lelloman.read.http.HttpClient
import com.lelloman.read.http.HttpRequest
import com.lelloman.read.utils.UrlValidator
import io.reactivex.Maybe

class FeedFinderHttpClient(
    private val httpClient: HttpClient,
    private val urlValidator: UrlValidator
) {

    fun requestStringBodyAndBaseUrl(url: String): Maybe<BaseUrlAndStringBody> = urlValidator
        .findBaseUrlWithProtocol(url)
        .flatMapSingle { baseUrl ->
            httpClient
                .request(HttpRequest(baseUrl))
                .map { it to baseUrl }
        }
        .filter { (httpResponse, _) ->
            httpResponse.isSuccessful && httpResponse.body.isNotEmpty()
        }
        .map { (httpResponse, baseUrl) ->
            BaseUrlAndStringBody(
                baseUrl = baseUrl,
                stringBody = httpResponse.stringBody
            )
        }
        .onErrorComplete()
}