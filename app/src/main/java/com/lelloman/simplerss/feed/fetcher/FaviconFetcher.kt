package com.lelloman.simplerss.feed.fetcher

import android.support.annotation.VisibleForTesting
import com.lelloman.common.utils.UrlValidator
import com.lelloman.simplerss.http.HttpClient
import com.lelloman.simplerss.http.HttpRequest
import com.lelloman.simplerss.http.HttpResponse
import io.reactivex.Maybe

class FaviconFetcher(
    private val httpClient: HttpClient,
    private val urlValidator: UrlValidator
) {

    fun getPngFavicon(url: String): Maybe<ByteArray> = getPngFaviconInternal(url, ::getDuckDuckGoFaviconUrl)
        .switchIfEmpty(getPngFaviconInternal(url, ::getGoogleS2FaviconUrl))
        .onErrorComplete()

    private fun getPngFaviconInternal(
        url: String,
        faviconUrlProvider: (String) -> String
    ): Maybe<ByteArray> = urlValidator
        .findBaseUrlWithoutProtocol(url)
        .flatMapSingle { baseUrl ->
            httpClient.request(HttpRequest(faviconUrlProvider(baseUrl)))
        }
        .filter { response ->
            response.isSuccessful && response.body.isNotEmpty()
        }
        .map(HttpResponse::body)
        .onErrorComplete()

    @VisibleForTesting
    fun getGoogleS2FaviconUrl(baseUrl: String) = "http://www.google.com/s2/favicons?domain=$baseUrl"

    @VisibleForTesting
    fun getDuckDuckGoFaviconUrl(baseUrl: String) = "https://icons.duckduckgo.com/ip3/$baseUrl.ico"
}