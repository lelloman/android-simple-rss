package com.lelloman.simplerss.feed.fetcher

import androidx.annotation.VisibleForTesting
import com.lelloman.common.http.HttpClient
import com.lelloman.common.http.HttpResponse
import com.lelloman.common.http.request.HttpRequest
import com.lelloman.common.logger.LoggerFactory
import com.lelloman.common.utils.UrlValidator
import io.reactivex.Maybe

class FaviconFetcher(
    private val httpClient: HttpClient,
    private val urlValidator: UrlValidator,
    private val bitmapDecoder: BitmapDecoder,
    loggerFactory: LoggerFactory
) {

    private val logger = loggerFactory.getLogger(javaClass)

    fun getPngFavicon(url: String): Maybe<ByteArray> =
        getPngFaviconInternal(url, ::getDuckDuckGoFaviconUrl)
            .switchIfEmpty(getPngFaviconInternal(url, ::getGoogleS2FaviconUrl))
            .onErrorComplete()

    private fun getPngFaviconInternal(
        url: String,
        faviconUrlProvider: (String) -> String
    ): Maybe<ByteArray> = urlValidator
        .findBaseUrlWithoutProtocol(url)
        .flatMapSingle { baseUrl ->
            logger.d("getPngFaviconInternal($url) base url $baseUrl")
            httpClient.request(HttpRequest(faviconUrlProvider(baseUrl)))
        }
        .filter { response ->
            logger.d("getPngFaviconInternal($url) http response successful ${response.isSuccessful} body length ${response.body.size}")
            response.isValidFaviconResponse()
        }
        .map(HttpResponse::body)
        .filter {
            bitmapDecoder.decodeBitmap(it) != null
        }
        .onErrorComplete()

    @VisibleForTesting
    fun getGoogleS2FaviconUrl(baseUrl: String) = "http://www.google.com/s2/favicons?domain=$baseUrl"

    @VisibleForTesting
    fun getDuckDuckGoFaviconUrl(baseUrl: String) = "https://icons.duckduckgo.com/ip3/$baseUrl.ico"

    private fun HttpResponse.isValidFaviconResponse() =
        isSuccessful && body.isNotEmpty() // TODO add content type check when available in common
}