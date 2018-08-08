package com.lelloman.read.feed.finder

import com.lelloman.read.core.logger.LoggerFactory
import com.lelloman.read.feed.fetcher.FeedFetcher
import com.lelloman.read.feed.fetcher.Success
import io.reactivex.Observable

class FeedFinder(
    private val httpClient: FeedFinderHttpClient,
    private val parser: FeedFinderParser,
    private val feedFetcher: FeedFetcher,
    loggerFactory: LoggerFactory
) {

    private val logger = loggerFactory.getLogger(javaClass.simpleName)

    fun findValidFeedUrls(url: String): Observable<FoundFeed> = httpClient
        .requestStringBodyAndBaseUrl(url)
        .flatMap { (stringBody, baseUrl) ->
            logger.d("findValidFeedUrls() base url $baseUrl")
            parser.parseDoc(
                url = baseUrl,
                html = stringBody
            )
        }
        .flatMapObservable(parser::findCandidateUrls)
        .flatMap { candidateUrl ->
            httpClient
                .requestStringBody(candidateUrl)
                .flatMap {
                    parser.parseDoc(
                        url = url,
                        html = it
                    )
                }
                .flatMapObservable {
                    Observable.merge(
                        parser.findCandidateUrls(it),
                        Observable.just(candidateUrl)
                    )
                }
        }
        .toList()
        .map { it.toSet() }
        .flatMapObservable { urls ->
            logger.d("found ${urls.size} urls to test")
            Observable.fromIterable(urls)
        }
        .flatMapMaybe(::testUrl)
        .onErrorResumeNext { _: Throwable ->
            Observable.empty()
        }

    private fun testUrl(urlToTest: String) = feedFetcher
        .testUrl(urlToTest)
        .filter { testResult ->
            logger.d("tested url $urlToTest -> $testResult")
            testResult is Success
        }
        .map { it as Success }
        .map {
            FoundFeed(
                url = urlToTest,
                nArticles = it.nArticles
            )
        }
}