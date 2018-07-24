package com.lelloman.read.feed

import com.lelloman.read.core.HtmlParser
import com.lelloman.read.http.HttpClient
import com.lelloman.read.http.HttpRequest
import com.lelloman.read.http.HttpResponse
import com.lelloman.read.utils.UrlValidator
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction

class FeedFinder(
    private val httpClient: HttpClient,
    private val urlValidator: UrlValidator,
    private val htmlParser: HtmlParser
) {

    fun findValidFeedUrls(url: String): Observable<String> = urlValidator
        .findBaseUrlWithoutProtocol(url)
        .flatMapSingle { baseUrl ->
            Single.zip(
                httpClient.request(HttpRequest(baseUrl)),
                urlValidator.findBaseUrlWithoutProtocol(url).toSingle(),
                BiFunction<HttpResponse, String, Pair<HttpResponse, String>> { httpResponse, baseUrl ->
                    Pair(httpResponse, baseUrl)
                }
            )

        }
        .filter { (httpResponse, _) ->
            httpResponse.isSuccessful && httpResponse.body.isNotEmpty()
        }
        .map { (httpResponse, baseUrl) ->
            httpResponse.stringBody to baseUrl
        }
        .flatMapObservable { (stringBody, baseUrl) ->
            findUrlCandidates(url = baseUrl, homeHtml = stringBody)
        }
        .map { urlValidator.maybePrependProtocol(it) }

    private fun findUrlCandidates(url: String, homeHtml: String): Observable<String> = Single
        .fromCallable {
            mutableListOf("$url/feed").apply {
                htmlParser
                    .parseLinkTagsInHead(homeHtml)
                    .forEach { (type, href) ->
                        when (type) {
                            "application/rss+xml",
                            "text/xml",
                            "application/atom+xml" -> add(href)
                        }
                    }
            }

//            <link rel="alternate" type="application/rss+xml" title="RSS 2.0" href="https://www.fanpage.it/feed/" />
//            <link rel="alternate" type="text/xml" title="RSS .92" href="https://www.fanpage.it/feed/rss/" />
//            <link rel="alternate" type="application/atom+xml" title="Atom 0.3" href="https://www.fanpage.it/feed/atom/" />
        }
        .flatMapObservable { Observable.fromIterable(it) }
}