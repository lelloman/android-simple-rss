package com.lelloman.read.feed.finder

import com.lelloman.read.html.HtmlParser
import com.lelloman.read.html.element.ADocElement
import com.lelloman.read.html.element.LinkDocElement
import com.lelloman.read.utils.UrlValidator
import io.reactivex.Observable
import io.reactivex.Single

class FeedFinderParser(
    private val urlValidator: UrlValidator,
    private val htmlParser: HtmlParser
) {
    fun findCandidateUrls(url: String, homeHtml: String): Observable<String> = Single
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