package com.lelloman.domain_sources.internal

import com.lelloman.domain_sources.Source
import com.lelloman.domain_sources.SourceItem
import com.lelloman.domain_sources.SourceOperationsProducer
import com.lelloman.domain_sources.SourcesRepository
import com.lelloman.simplerss.domain_feed.FeedItem
import com.lelloman.simplerss.domain_feed.FeedSource
import com.lelloman.simplerss.domain_feed.FeedSourceOperationsProducer
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject

class SourcesRepositoryImpl(
    sourceOperationsProducers: Set<SourceOperationsProducer>,
    private val ioScheduler: Scheduler = Schedulers.io()
) : SourcesRepository, FeedSourceOperationsProducer {

    private val sources = HashMap<String, Source>()

    private val feedSourceOperationsSubject = PublishSubject.create<List<FeedSource.Operation>>()

    init {
        sourceOperationsProducers.forEach { sourceProducer ->
            sourceProducer.produceSourceOperations()
                .subscribeOn(ioScheduler)
                .observeOn(ioScheduler)
                .subscribe(::handleSourceOperations)
        }
    }

    override fun produceFeedSourceOperations(): Observable<List<FeedSource.Operation>> = synchronized(this) {
        val pendingAddedSources = sources.values.map { FeedSource.Operation.Add(FeedSourceImpl(it)) }
        return feedSourceOperationsSubject.hide()
            .startWith(Observable.just(pendingAddedSources))
    }

    private fun handleSourceOperations(operations: List<Source.Operation>) = synchronized(this) {
        operations.forEach { operation ->
            val feedSourceOperations = mutableListOf<FeedSource.Operation>()
            when (operation) {
                is Source.Operation.Add -> {
                    feedSourceOperations.add(FeedSource.Operation.Add(FeedSourceImpl(operation.source)))
                    sources[operation.source.id] = operation.source
                }
                is Source.Operation.Remove -> {
                    feedSourceOperations.add(FeedSource.Operation.Remove(operation.sourceId))
                    sources.remove(operation.sourceId)
                }
                is Source.Operation.Update -> {
                    sources[operation.source.id] = operation.source
                }
            }
            feedSourceOperationsSubject.onNext(feedSourceOperations)
        }
    }

    private class FeedSourceImpl(
        private val source: Source
    ) : FeedSource {
        override val id: String = source.id

        override fun observeItems(): Observable<List<FeedItem>> {
            return source.observeItems().map { items -> items.map(::FeedItemImpl) }
        }

        override fun refresh(): Completable = source.refresh()
    }

    private class FeedItemImpl(sourceItem: SourceItem) : SourceItem by sourceItem, FeedItem
}