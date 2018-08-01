package com.lelloman.read.feed.finder

import com.lelloman.read.http.HttpClient
import com.lelloman.read.http.HttpRequest
import com.lelloman.read.utils.UrlValidator
import io.reactivex.Maybe
import io.reactivex.Single

class FeedFinderHttpClient(
    private val httpClient: HttpClient,
    private val urlValidator: UrlValidator
) {

    fun requestStringBody(url: String): Maybe<String> = Single
        .just(urlValidator.maybePrependProtocol(url))
        .flatMapMaybe {
            httpClient.request(HttpRequest(url))
                .filter { it.isSuccessful && it.stringBody.isNotEmpty() }
                .map { it.stringBody }
        }

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
}