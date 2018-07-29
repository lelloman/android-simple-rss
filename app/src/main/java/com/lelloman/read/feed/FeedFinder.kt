package com.lelloman.read.feed

import com.lelloman.read.html.HtmlParser
import com.lelloman.read.html.element.ADocElement
import com.lelloman.read.html.element.LinkDocElement
import com.lelloman.read.http.HttpClient
import com.lelloman.read.http.HttpRequest
import com.lelloman.read.utils.UrlValidator
import io.reactivex.Observable
import io.reactivex.Single

class FeedFinder(
    private val httpClient: HttpClient,
    private val urlValidator: UrlValidator,
    private val htmlParser: HtmlParser,
    private val feedFetcher: FeedFetcher
) {

    fun findValidFeedUrls(url: String): Observable<String> = urlValidator
        .findBaseUrlWithProtocol(url)
        .flatMapSingle { baseUrl ->
            httpClient
                .request(HttpRequest(baseUrl))
                .map { it to baseUrl }
        }
        .filter { (httpResponse, _) ->
            httpResponse.isSuccessful && httpResponse.body.isNotEmpty()
        }
        .map { (httpResponse, baseUrl) ->
            httpResponse.stringBody to baseUrl
        }
        .flatMapObservable { (stringBody, baseUrl) ->
            findCandidateUrls(url = baseUrl, homeHtml = stringBody)
        }
        .flatMapMaybe { urlToTest ->
            feedFetcher
                .testUrl(urlToTest)
                .filter { it == FeedFetcher.TestResult.SUCCESS }
                .map { urlToTest }
        }
        .onErrorResumeNext { _: Throwable ->
            Observable.empty()
        }

    private fun findCandidateUrls(url: String, homeHtml: String): Observable<String> = Single
        .fromCallable {
            mutableListOf("$url/feed")
                .apply {
                    htmlParser
                        .parseDoc(homeHtml)
                        .iterate { element ->
                            when (element) {
                                is LinkDocElement -> {
                                    when (element.linkType) {
                                        "application/rss+xml",
                                        "text/xml",
                                        "application/atom+xml" -> add(element.href)
                                    }
                                }
                                is ADocElement -> {
                                    if (element.href.contains(Regex("(feed|rss)"))) {
                                        add(element.href)
                                    }
                                }
                            }
                        }
                }
                .map {
                    urlValidator.maybePrependBaseUrl(
                        baseUrl = url,
                        path = it
                    )
                }
        }
        .flatMapObservable { Observable.fromIterable(it) }
}