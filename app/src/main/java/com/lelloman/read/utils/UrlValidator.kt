package com.lelloman.read.utils

import android.util.Patterns
import io.reactivex.Maybe
import java.net.URL

interface UrlValidator {

    fun maybePrependBaseUrl(baseUrl: String, path: String): String

    fun findBaseUrlWithoutProtocol(url: String): Maybe<String>

    fun findBaseUrlWithProtocol(url: String): Maybe<String>

    fun isValidUrl(url: String?): Boolean

    fun maybePrependProtocol(url: String?): String?
}

class UrlValidatorImpl : UrlValidator {

    override fun maybePrependBaseUrl(baseUrl: String, path: String) = if (path.startsWith("/")) {
        baseUrl + path
    } else {
        path
    }

    override fun findBaseUrlWithoutProtocol(url: String): Maybe<String> = Maybe
        .fromCallable { URL(url).host }
        .onErrorComplete()

    override fun findBaseUrlWithProtocol(url: String): Maybe<String> = Maybe
        .fromCallable {
            URL(url).let {
                "${it.protocol}://${it.host}"
            }
        }
        .onErrorComplete()

    override fun isValidUrl(url: String?) = url?.let {
        Patterns.WEB_URL.matcher(url).matches()
    } ?: false

    override fun maybePrependProtocol(url: String?): String? {
        if (url == null) return null

        if (url.startsWith("http://") || url.startsWith("https://")) {
            return url
        }

        return "http://$url"
    }
}