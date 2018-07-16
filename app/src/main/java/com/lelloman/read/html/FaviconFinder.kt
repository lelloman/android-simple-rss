package com.lelloman.read.html

import android.support.annotation.VisibleForTesting
import com.lelloman.read.http.HttpClient
import com.lelloman.read.http.HttpRequest
import io.reactivex.Maybe

class FaviconFinder(
    private val httpClient: HttpClient,
    private val htmlParser: HtmlParser
) {

    @VisibleForTesting
    fun findBaseUrl(url: String): Maybe<String> = Maybe
        .fromCallable {
            url
        }
        .onErrorComplete()


    fun findFaviconUrl(url: String): Maybe<String> = findBaseUrl(url)
        .flatMapSingle {
            val request = HttpRequest(it)
            httpClient.request(request)
        }
        .filter { it.isSuccessful && it.body.isNotEmpty() }
        .map { it.body }
        .flatMapObservable {

        }

}