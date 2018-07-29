package com.lelloman.read.feed

import com.lelloman.read.html.HtmlParser
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
    private val htmlParser: HtmlParser,
    private val feedFetcher: FeedFetcher
) {

    fun findValidFeedUrls(url: String): Observable<String> = urlValidator
        .findBaseUrlWithProtocol(url)
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
            findCandidateUrls(url = baseUrl, homeHtml = stringBody)
        }
        .map { urlValidator.maybePrependProtocol(it) }
        .flatMapMaybe { urlToTest ->
            feedFetcher
                .testUrl(urlToTest)
                .filter { it == FeedFetcher.TestResult.SUCCESS }
                .map { urlToTest }
        }

    private fun findCandidateUrls(url: String, homeHtml: String): Observable<String> = Single
        .fromCallable {
            mutableListOf("$url/feed").apply {
                //                htmlParser
//                    .parseLinkTagsInHead(homeHtml)
//                    .forEach { (type, href) ->
//                        when (type) {
//                            "application/rss+xml",
//                            "text/xml",
//                            "application/atom+xml" -> add(href)
//                        }
//                    }
            }
        }
        .flatMapObservable { Observable.fromIterable(it) }
}