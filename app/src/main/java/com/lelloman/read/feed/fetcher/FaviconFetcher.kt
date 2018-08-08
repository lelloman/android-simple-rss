package com.lelloman.read.feed.fetcher

import android.support.annotation.VisibleForTesting
import com.lelloman.read.http.HttpClient
import com.lelloman.read.http.HttpRequest
import com.lelloman.read.utils.UrlValidator
import io.reactivex.Maybe

class FaviconFetcher(
    private val httpClient: HttpClient,
    private val urlValidator: UrlValidator
) {

    fun getPngFavicon(url: String): Maybe<ByteArray> = urlValidator.findBaseUrlWithoutProtocol(url)
        .flatMapSingle { baseUrl ->
            httpClient
                .request(HttpRequest(getGoogleS2FaviconUrl(baseUrl)))
        }
        .filter { response ->
            response.isSuccessful && response.body.isNotEmpty()
        }
        .map { it.body }
        .onErrorComplete()

    @VisibleForTesting
    fun getGoogleS2FaviconUrl(baseUrl: String) = "http://www.google.com/s2/favicons?domain=$baseUrl"

}