package com.lelloman.simplerss.feed.finder

import com.lelloman.common.http.HttpClient
import com.lelloman.common.http.HttpRequest
import com.lelloman.common.utils.UrlValidator
import io.reactivex.Maybe
import io.reactivex.Single

class FeedFinderHttpClient(
    private val httpClient: HttpClient,
    private val urlValidator: UrlValidator
) {

    fun requestStringBodyAndBaseUrl(url: String): Maybe<StringBodyAndUrl> = urlValidator
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
            StringBodyAndUrl(
                url = baseUrl,
                stringBody = httpResponse.stringBody
            )
        }
        .onErrorComplete()

    fun requestStringBody(url: String): Maybe<String> = Single
        .just(urlValidator.maybePrependProtocol(url))
        .flatMapMaybe { urlWithProtocol ->
            httpClient.request(HttpRequest(urlWithProtocol))
                .filter { it.isSuccessful && it.stringBody.isNotEmpty() }
                .map { it.stringBody }
        }
}