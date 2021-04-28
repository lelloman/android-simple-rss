package com.lelloman.simplerss.domain_local_sources.internal

import com.lelloman.simplerss.domain_feed.FeedSource
import com.lelloman.simplerss.domain_feed.FeedSourceOperationsProducer
import com.lelloman.simplerss.domain_local_sources.LocalSource
import com.lelloman.simplerss.domain_local_sources.LocalSourcesRepository
import com.lelloman.simplerss.domain_local_sources.internal.db.LocalSourceEntity
import com.lelloman.simplerss.domain_local_sources.internal.db.LocalSourcesDao
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject

internal class LocalSourcesRepositoryImpl(
    private val localSourcesDao: LocalSourcesDao,
    private val adapter: LocalSourceAdapter,
    private val writeScheduler: Scheduler = Schedulers.newThread()
) : LocalSourcesRepository, FeedSourceOperationsProducer {

    private val id: String = "LocalSources"

    private val type: FeedSource.Type = LocalSourceType

    private val sourcesOperationsSubject = PublishSubject.create<List<FeedSource.Operation>>()

    override fun produceFeedSourceOperations(): Observable<List<FeedSource.Operation>> {
        return sourcesOperationsSubject.hide()
        // TODO .startWith()
    }

    override fun observeLocalSources(): Observable<List<LocalSource>> {
        return localSourcesDao.observe().map { it as List<LocalSource> }
    }

    override fun delete(localSourceId: Long): Completable = localSourcesDao.delete(localSourceId)
        .subscribeOn(writeScheduler)
        .doOnComplete { emit(FeedSource.Operation.Remove(adapter.localSourceIdToSourceId(localSourceId))) }

    override fun update(localSource: LocalSource): Completable = localSource.toEntity()
        .let(localSourcesDao::update)
        .subscribeOn(writeScheduler)

    override fun add(name: String, url: String): Completable {
        val localSource = makeSourceEntityToAdd(name = name, url = url)
        return localSource
            .let(localSourcesDao::insert)
            .subscribeOn(writeScheduler)
            .doOnSuccess { emit(FeedSource.Operation.Add(adapter.fromLocalSource(localSource, it))) }
            .ignoreElement()
    }

    private fun emit(operation: FeedSource.Operation) {
        sourcesOperationsSubject.onNext(listOf(operation))
    }

    private fun LocalSource.toEntity() = LocalSourceEntity(
        id = id,
        name = name,
        url = url,
        lastRefresh = lastRefresh,
        isActive = isActive,
        icon = icon
    )

    private fun makeSourceEntityToAdd(name: String, url: String) = LocalSourceEntity(
        name = name,
        url = url,
        id = 0L,
        lastRefresh = 0L,
        isActive = true,
        icon = null
    )

}