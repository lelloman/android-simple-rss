package com.lelloman.read.utils

import android.util.Patterns

class UrlValidator {

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