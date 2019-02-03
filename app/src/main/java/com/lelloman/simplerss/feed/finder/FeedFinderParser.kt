package com.lelloman.simplerss.feed.finder

import com.lelloman.common.logger.LoggerFactory
import com.lelloman.common.utils.UrlValidator
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

class FeedFinderParser(
    private val urlValidator: UrlValidator,
    private val htmlParser: com.lelloman.simplerss.html.HtmlParser,
    loggerFactory: LoggerFactory
) {

    private val logger = loggerFactory.getLogger(javaClass)

    fun parseDoc(url: String, html: String): Maybe<com.lelloman.simplerss.html.Doc> = urlValidator
        .findBaseUrlWithProtocol(url)
        .flatMap { baseUrl ->
            Maybe.fromCallable {
                htmlParser.parseDoc(url = url,
                    baseUrl = baseUrl,
                    html = html
                )
            }
        }
        .onErrorComplete()

    fun findCandidateUrls(doc: com.lelloman.simplerss.html.Doc): Observable<String> = Single
        .fromCallable {
            val output = if (doc.url != null) {
                mutableListOf("${doc.url}/feed")
            } else {
                mutableListOf()
            }
            output
                .apply {
                    doc.iterate { element ->
                        when (element) {
                            is com.lelloman.simplerss.html.element.LinkDocElement -> {
                                when (element.linkType) {
                                    "application/rss+xml",
                                    "text/xml",
                                    "application/atom+xml" -> add(element.href)
                                }
                            }
                            is com.lelloman.simplerss.html.element.ADocElement -> {
                                if (element.href.contains(Regex("(feed|rss)"))) {
                                    add(element.href)
                                }
                            }
                        }
                    }
                }
                .map { url ->
                    logger.d("found url candidate $url")
                    urlValidator.maybePrependBaseUrl(
                        baseUrl = doc.baseUrl ?: "",
                        path = url
                    )
                }
        }
        .map { it.toSet() }
        .flatMapObservable { Observable.fromIterable(it) }
}