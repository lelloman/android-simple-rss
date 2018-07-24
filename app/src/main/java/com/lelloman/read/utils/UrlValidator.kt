package com.lelloman.read.utils

import android.util.Patterns
import io.reactivex.Maybe
import java.net.URL

class UrlValidator {

    fun findBaseUrlWithoutProtocol(url: String): Maybe<String> = Maybe
        .fromCallable { URL(url).host }
        .onErrorComplete()

    fun findBaseUrlWithProtocol(url: String): Maybe<String> = Maybe
        .fromCallable {
            URL(url).let {
                "${it.protocol}://${it.host}"
            }
        }
        .onErrorComplete()

    fun isValidUrl(url: String?) = url?.let {
        Patterns.WEB_URL.matcher(url).matches()
    } ?: false

    fun maybePrependProtocol(url: String?): String? {
        if (url == null) return null

        if (url.startsWith("http://") || url.startsWith("https://")) {
            return url
        }

        return "http://$url"
    }
}