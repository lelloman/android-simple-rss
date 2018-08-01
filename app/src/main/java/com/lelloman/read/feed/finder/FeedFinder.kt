package com.lelloman.read.feed.finder

import com.lelloman.read.core.logger.LoggerFactory
import com.lelloman.read.feed.FeedFetcher
import io.reactivex.Observable

class FeedFinder(
    private val httpClient: FeedFinderHttpClient,
    private val parser: FeedFinderParser,
    private val feedFetcher: FeedFetcher,
    loggerFactory: LoggerFactory
) {

    private val logger = loggerFactory.getLogger(javaClass.simpleName)

    fun findValidFeedUrls(url: String): Observable<String> = httpClient
        .requestStringBodyAndBaseUrl(url)
        .flatMap { (stringBody, baseUrl) ->
            logger.d("findValidFeedUrls() base url $baseUrl")
            parser.parseDoc(
                url = baseUrl,
                html = stringBody
            )
        }
        .flatMapObservable { doc ->
            parser
                .findCandidateUrls(doc)
        }
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
        .doOnSuccess { urlCandidates ->
            logger.d("gathered ${urlCandidates.size} url candidates:")
            urlCandidates.forEach { logger.d("candidate: $it") }
        }
        .flatMapObservable { Observable.fromIterable(it) }
        .flatMapMaybe { urlToTest ->
            logger.d("testing url candidate $urlToTest")
            feedFetcher
                .testUrl(urlToTest)
                .filter { it == FeedFetcher.TestResult.SUCCESS }
                .map { urlToTest }
        }
        .onErrorResumeNext { _: Throwable ->
            Observable.empty()
        }
}