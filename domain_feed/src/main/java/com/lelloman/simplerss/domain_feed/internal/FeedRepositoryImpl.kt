package com.lelloman.simplerss.domain_feed.internal

import com.lelloman.simplerss.domain_feed.FeedItem
import com.lelloman.simplerss.domain_feed.FeedRepository
import com.lelloman.simplerss.domain_feed.FeedSource
import com.lelloman.simplerss.domain_feed.FeedSourceOperationsProducer
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class FeedRepositoryImpl(
    feedSourcesOperationsProducers: Set<FeedSourceOperationsProducer>,
    private val ioScheduler: Scheduler = Schedulers.io()
) : FeedRepository {

    private var operationProducersSubscriptions = CompositeDisposable()

    private val sourcesSubscriptions = HashMap<String, Disposable>()

    private val feedSubject = BehaviorSubject.createDefault(emptyList<FeedItem>())

    init {
        feedSourcesOperationsProducers.forEach { feedSourceOperationProducer ->
            feedSourceOperationProducer.produceFeedSourceOperations()
                .subscribeOn(ioScheduler)
                .observeOn(ioScheduler)
                .onErrorComplete()
                .subscribe(::handleFeedSourceOperations)
                .let(operationProducersSubscriptions::add)
        }
    }

    override fun observeFeed(): Observable<List<FeedItem>> {
        return Single.timer(2, TimeUnit.SECONDS)
            .flatMapObservable {
                feedSubject.hide()
            }
    }

    override fun refreshFeed(): Completable {
        return Completable.timer(2, TimeUnit.SECONDS)
    }

    override fun dispose() {
        operationProducersSubscriptions.dispose()
    }

    private fun handleFeedSourceOperations(operations: List<FeedSource.Operation>) = operations.forEach { operation ->
        when (operation) {
            is FeedSource.Operation.Add -> addSource(operation.source)
            is FeedSource.Operation.Remove -> removeSource(operation.sourceId)
        }
    }

    private fun addSource(source: FeedSource) {
        if (sourcesSubscriptions.containsKey(source.id)) {
            // TODO warning?
            sourcesSubscriptions[source.id]?.dispose()
        }

        sourcesSubscriptions[source.id] = source.observeItems()
            .subscribeOn(ioScheduler)
            .observeOn(ioScheduler)
            .onErrorComplete()
            .subscribe { onNewFeedItems(source, it) }
    }

    private fun removeSource(sourceId: String) {
        sourcesSubscriptions[sourceId]?.dispose()
        sourcesSubscriptions.remove(sourceId)
    }

    private fun onNewFeedItems(source: FeedSource, items: List<FeedItem>) {
        // TODO use Map<FeedSource, List<FeedItem>> instead
        val currentFeedItems = feedSubject.value ?: emptyList()
        val newFeed = ArrayList<FeedItem>(currentFeedItems.size * 2)
        currentFeedItems.forEach { currentFeedItem ->
            if (currentFeedItem.sourceId != source.id) {
                newFeed.add(currentFeedItem)
            }
        }
        newFeed.addAll(items)
        newFeed.sortByDescending { it.time }
        feedSubject.onNext(newFeed)
    }
}