package com.lelloman.read.feed.finder

import com.lelloman.read.feed.FeedFetcher
import io.reactivex.Observable

class FeedFinder(
    private val httpClient: FeedFinderHttpClient,
    private val parser: FeedFinderParser,
    private val feedFetcher: FeedFetcher
) {

    fun findValidFeedUrls(url: String): Observable<String> = httpClient
        .requestStringBodyAndBaseUrl(url)
        .flatMapObservable { (stringBody, baseUrl) ->
            parser.findCandidateUrls(url = baseUrl, homeHtml = stringBody)
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
}