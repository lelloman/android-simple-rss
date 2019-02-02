package com.lelloman.read.feed.finder

import com.lelloman.common.logger.LoggerFactory
import com.lelloman.read.feed.fetcher.FeedFetcher
import com.lelloman.read.feed.fetcher.Success
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject


interface FeedFinder {
    val loading: Observable<Boolean>
    fun findValidFeedUrls(url: String): Observable<FoundFeed>
}

internal class FeedFinderImpl(
    private val httpClient: FeedFinderHttpClient,
    private val parser: FeedFinderParser,
    private val feedFetcher: FeedFetcher,
    private val newThreadScheduler: Scheduler,
    loggerFactory: LoggerFactory
) : FeedFinder {

    private val logger = loggerFactory.getLogger(javaClass)

    private val loadingSubject: Subject<Boolean> = BehaviorSubject.create()

    override val loading: Observable<Boolean> = loadingSubject.hide()

    private var nextId = 1L

    init {
        loadingSubject.onNext(false)
    }

    override fun findValidFeedUrls(url: String): Observable<FoundFeed> = mutableSetOf<String>().let { foundUrls ->
        httpClient
            .requestStringBodyAndBaseUrl(url)
            .subscribeOn(newThreadScheduler)
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
                    .subscribeOn(newThreadScheduler)
                    .onErrorComplete()
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
            .filter { foundUrls.contains(it).not() }
            .doOnNext { foundUrls.add(it) }
            .flatMapMaybe(::testUrl)
            .onErrorResumeNext { throwable: Throwable ->
                logger.e("error", throwable)
                Observable.empty()
            }
            .doOnSubscribe { loadingSubject.onNext(true) }
            .doFinally { loadingSubject.onNext(false) }
    }

    private fun testUrl(urlToTest: String) = feedFetcher
        .testUrl(urlToTest)
        .subscribeOn(newThreadScheduler)
        .filter { testResult ->
            logger.d("tested url $urlToTest -> $testResult")
            testResult is Success
        }
        .map { it as Success }
        .map {
            FoundFeed(
                id = nextId++,
                url = urlToTest,
                nArticles = it.nArticles,
                name = it.title
            )
        }
}