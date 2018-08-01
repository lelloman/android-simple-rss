package com.lelloman.read.feed.finder

import com.lelloman.read.core.logger.LoggerFactory
import com.lelloman.read.html.Doc
import com.lelloman.read.html.HtmlParser
import com.lelloman.read.html.element.ADocElement
import com.lelloman.read.html.element.LinkDocElement
import com.lelloman.read.utils.UrlValidator
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

class FeedFinderParser(
    private val urlValidator: UrlValidator,
    private val htmlParser: HtmlParser,
    loggerFactory: LoggerFactory
) {

    private val logger = loggerFactory.getLogger(javaClass.simpleName)

    fun parseDoc(url: String, html: String): Maybe<Doc> = urlValidator
        .findBaseUrlWithProtocol(url)
        .flatMap { baseUrl ->
            Maybe.fromCallable {
                htmlParser.parseDoc(url = url,
                    baseUrl = baseUrl,
                    html = html
                )
            }
        }
        .map {
            logger.d("doc parsed $it")
            it
        }
//        .onErrorComplete()

    fun findAllLinks(doc: Doc): Observable<String> = Single
        .fromCallable {
            logger.d("findAllLinks for doc with url ${doc.url}")
            doc
                .all()
                .filter { it is ADocElement }
                .map { it as ADocElement }
                .map { it.href }
        }
        .map {
            logger.d("findAllLinks() doc url ${doc.url} found ${it.size} links")
            it.toSet()
        }
        .flatMapObservable { Observable.fromIterable(it) }

    fun findCandidateUrls(doc: Doc): Observable<String> = Single
        .fromCallable {
            logger.d("findCandidateUrls() doc url ${doc.url}")
            val output = if (doc.url != null) {
                mutableListOf("${doc.url}/feed")
            } else {
                mutableListOf()
            }
            output
                .apply {
                    doc.iterate { element ->
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
                .map { url ->
                    logger.d("found url candidate $url")
                    urlValidator.maybePrependBaseUrl(
                        baseUrl = doc.url ?: "",
                        path = url
                    )
                }
        }
        .map { it.toSet() }
        .flatMapObservable { Observable.fromIterable(it) }
}