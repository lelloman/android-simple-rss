package com.lelloman.read.ui.common.repository

import com.lelloman.common.di.qualifiers.IoScheduler
import com.lelloman.common.logger.LoggerFactory
import com.lelloman.read.feed.finder.FeedFinder
import com.lelloman.read.feed.finder.FoundFeed
import com.lelloman.read.persistence.db.SourcesDao
import com.lelloman.read.persistence.db.model.Source
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.ReplaySubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiscoverRepository @Inject constructor(
    @IoScheduler private val ioScheduler: Scheduler,
    private val sourcesDao: SourcesDao,
    private val feedFinder: FeedFinder,
    loggerFactory: LoggerFactory
) {

    private val logger = loggerFactory.getLogger(javaClass.simpleName)

    private val isFindingFeedsSubject = BehaviorSubject
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

    fun reset(){
        lastFindFeedUrl = null
        findFeedSubscription?.dispose()
        foundFeedsList.clear()
        isFindingFeedsSubject.onNext(false)
        foundFeedsSubject.cleanupBuffer()
    }

    fun findFeeds(url: String) {
        if (isFindingFeeds.blockingFirst()) {
            if (url == lastFindFeedUrl) {
                return
            } else {
                findFeedSubscription?.dispose()
                findFeedSubscription = null
            }
        }

        lastFindFeedUrl = url
        foundFeedsList.clear()
        foundFeedsSubject.cleanupBuffer()
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

        findFeedSubscription = Observable
            .combineLatest<FoundFeed, List<Source>, Pair<FoundFeed, List<Source>>>(
                foundFeedsStream,
                sourcesDao.getAll().toObservable(),
                BiFunction { foundFeed, allSources ->
                    foundFeed to allSources
                }
            )
            .subscribe { (foundFeed, allSources) ->
                foundFeedsList.add(foundFeed)
                foundFeedsList.removeAll { oldFoundFeed ->
                    val alreadyInSources = allSources.any { it.url == oldFoundFeed.url }
                    if (alreadyInSources) {
                        logger.d("Filtering out found feed $oldFoundFeed because already in sources.")
                    }
                    alreadyInSources
                }
                logger.d("onNexting foundFeedList with size ${foundFeedsList.size}")
                foundFeedsSubject.cleanupBuffer()
                foundFeedsSubject.onNext(foundFeedsList)
            }
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