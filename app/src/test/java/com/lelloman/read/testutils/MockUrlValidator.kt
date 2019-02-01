package com.lelloman.read.testutils

import com.lelloman.common.utils.UrlValidator
import io.reactivex.Maybe

class MockUrlValidator(action: MockUrlValidator.() -> Unit = {}) : UrlValidator {

    init {
        action.invoke(this)
    }

    var findBaseUrlWithProtocolThrowsException = false
    var foundBaseUrlWithProtocol: String? = null

    private var maybePrependBaseUrlMap = mutableMapOf<Pair<String, String>, String>()

    fun wheneverMaybePrependBaseUrl(baseUrl: String, path: String, action: () -> String) {
        maybePrependBaseUrlMap[baseUrl to path] = action.invoke()
    }

    override fun maybePrependBaseUrl(baseUrl: String, path: String): String {
        return if (maybePrependBaseUrlMap.containsKey(baseUrl to path)) {
            maybePrependBaseUrlMap[baseUrl to path]!!
        } else {
            path
        }
    }

    override fun findBaseUrlWithoutProtocol(url: String): Maybe<String> = Maybe.just(url)

    override fun findBaseUrlWithProtocol(url: String): Maybe<String> = if (findBaseUrlWithProtocolThrowsException) {
        Maybe.error(Exception("CRASH"))
    } else {
        Maybe.just(foundBaseUrlWithProtocol ?: url)
    }

    override fun isValidUrl(url: String?) = true

    override fun maybePrependProtocol(url: String?) = url
}
