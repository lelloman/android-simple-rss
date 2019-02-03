package com.lelloman.simplerss.ui.common.repository

import com.lelloman.common.di.qualifiers.IoScheduler
import com.lelloman.common.logger.LoggerFactory
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiscoverRepository @Inject constructor(
    @IoScheduler private val ioScheduler: Scheduler,
    private val sourcesDao: com.lelloman.simplerss.persistence.db.SourcesDao,
    private val feedFinder: com.lelloman.simplerss.feed.finder.FeedFinder,
    loggerFactory: LoggerFactory
) {

    private val logger = loggerFactory.getLogger(javaClass)

    private val isFindingFeedsSubject = BehaviorSubject
        .create<Boolean>()
        .apply { onNext(false) }

    val isFindingFeeds: Observable<Boolean> = isFindingFeedsSubject
        .hide()
        .distinctUntilChanged()

    private var lastFindFeedUrl: String? = null
    private var findFeedSubscriptions = CompositeDisposable()

    private val foundFeedsList = mutableListOf<com.lelloman.simplerss.feed.finder.FoundFeed>()
    private val foundFeedsSubject = BehaviorSubject.create<List<com.lelloman.simplerss.feed.finder.FoundFeed>>()
    val foundFeeds: Observable<List<com.lelloman.simplerss.feed.finder.FoundFeed>> = foundFeedsSubject.hide()

    fun reset() {
        lastFindFeedUrl = null
        findFeedSubscriptions.clear()
        foundFeedsList.clear()
        isFindingFeedsSubject.onNext(false)
        foundFeedsSubject.onNext(emptyList())
    }

    fun findFeeds(url: String) {
        if (isFindingFeeds.blockingFirst()) {
            logger.w("findFeeds($url) called but is already finding feeds for url $lastFindFeedUrl.")
            return
        }

        lastFindFeedUrl = url
        foundFeedsList.clear()
        foundFeedsSubject.onNext(foundFeedsList)

        val foundFeedsStream = feedFinder
            .findValidFeedUrls(url)
            .subscribeOn(ioScheduler)
            .observeOn(ioScheduler)
            .doOnSubscribe { isFindingFeedsSubject.onNext(true) }
            .doAfterTerminate {
                isFindingFeedsSubject.onNext(false)
                lastFindFeedUrl = null
            }

        findFeedSubscriptions.add(foundFeedsStream
            .subscribe { foundFeed ->
                foundFeedsList.add(foundFeed)
                logger.d("onNexting foundFeedList with size ${foundFeedsList.size}")
                foundFeedsSubject.onNext(foundFeedsList)
            }
        )
    }

    fun addFoundFeeds(foundFeeds: List<com.lelloman.simplerss.feed.finder.FoundFeed>): Completable = Completable.fromAction {
        foundFeeds.forEach {
            sourcesDao.insert(com.lelloman.simplerss.persistence.db.model.Source(
                name = it.name ?: it.url,
                url = it.url,
                isActive = true
            ))
        }
    }
}