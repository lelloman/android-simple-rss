package com.lelloman.read.ui.walkthrough.repository

import com.lelloman.read.core.di.qualifiers.IoScheduler
import com.lelloman.read.feed.finder.FeedFinder
import com.lelloman.read.feed.finder.FoundFeed
import com.lelloman.read.persistence.db.SourcesDao
import com.lelloman.read.persistence.db.model.Source
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.ReplaySubject
import io.reactivex.subjects.Subject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WalkthroughRepository @Inject constructor(
    @IoScheduler private val ioScheduler: Scheduler,
    private val sourcesDao: SourcesDao,
    private val feedFinder: FeedFinder
) {

    private val isFindingFeedsSubject: Subject<Boolean> = BehaviorSubject
        .create<Boolean>()
        .apply { onNext(false) }

    val isFindingFeeds: Observable<Boolean> = isFindingFeedsSubject
        .hide()
        .distinctUntilChanged()

    private var lastFindFeedUrl: String? = null
    private var findFeedSubscription: Disposable? = null

    private val foundFeedsList = mutableListOf<FoundFeed>()
    private val foundFeedsSubject = ReplaySubject.create<List<FoundFeed>>()
    val foundFeeds: Observable<List<FoundFeed>> = foundFeedsSubject.hide()

    fun findFeeds(url: String) {
        if (isFindingFeeds.blockingFirst()) {
            if (url == lastFindFeedUrl) {
                return
            } else {
                findFeedSubscription?.dispose()
            }
        }

        lastFindFeedUrl = url
        foundFeedsList.clear()
        foundFeedsSubject.cleanupBuffer()
        foundFeedsSubject.onNext(foundFeedsList)

        findFeedSubscription = feedFinder
            .findValidFeedUrls(url)
            .subscribeOn(ioScheduler)
            .observeOn(ioScheduler)
            .doOnSubscribe { isFindingFeedsSubject.onNext(true) }
            .doAfterTerminate {
                isFindingFeedsSubject.onNext(false)
                lastFindFeedUrl = null
            }
            .subscribe {
                foundFeedsList.add(it)
                foundFeedsSubject.cleanupBuffer()
                foundFeedsSubject.onNext(foundFeedsList)
            }

//        foundFeedsList.addAll(Array(30){
//            FoundFeed(
//                id = it.toLong(),
//                url = "www.asdasdasdasd.com",
//                nArticles = 666,
//                name = "bla bla bla bla bla bla bla bla bla"
//            )
//        })
    }

    fun addFoundFeeds(foundFeeds: List<FoundFeed>): Completable = Completable.fromAction {
        foundFeeds.forEach {
            sourcesDao.insert(Source(
                name = it.name ?: it.url,
                url = it.url,
                isActive = true
            ))
        }
    }
}