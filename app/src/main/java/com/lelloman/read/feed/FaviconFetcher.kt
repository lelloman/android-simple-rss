package com.lelloman.read.feed

import android.support.annotation.VisibleForTesting
import com.lelloman.read.http.HttpClient
import com.lelloman.read.http.HttpRequest
import io.reactivex.Maybe
import java.net.URL


class FaviconFetcher(private val httpClient: HttpClient) {

    @VisibleForTesting
    fun findBaseUrl(url: String): Maybe<String> = Maybe
        .fromCallable { URL(url).host }
        .onErrorComplete()

    fun getPngFavicon(url: String): Maybe<ByteArray> = findBaseUrl(url)
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