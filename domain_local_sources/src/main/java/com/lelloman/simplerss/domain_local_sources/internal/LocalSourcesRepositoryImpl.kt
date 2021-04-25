package com.lelloman.simplerss.domain_local_sources.internal

import com.lelloman.domain_sources.Source
import com.lelloman.domain_sources.SourceOperationsProducer
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
) : LocalSourcesRepository, SourceOperationsProducer {

    override val id: String = "LocalSources"

    override val type: Source.Type = LocalSourceType

    private val sourcesOperationsSubject = PublishSubject.create<List<Source.Operation>>()

    override fun produceSourceOperations(): Observable<List<Source.Operation>> {
        return sourcesOperationsSubject.hide()
        // TODO .startWith()
    }

    override fun observeLocalSources(): Observable<List<LocalSource>> {
        return localSourcesDao.observe().map { it as List<LocalSource> }
    }

    override fun delete(localSourceId: Long): Completable = localSourcesDao.delete(localSourceId)
        .subscribeOn(writeScheduler)
        .doOnComplete { emit(Source.Operation.Remove(adapter.localSourceIdToSourceId(localSourceId))) }

    override fun update(localSource: LocalSource): Completable = localSource.toEntity()
        .let(localSourcesDao::update)
        .subscribeOn(writeScheduler)
        .doOnComplete { emit(Source.Operation.Update(adapter.fromLocalSource(localSource))) }

    override fun add(localSource: LocalSource): Completable = localSource.toEntity()
        .let(localSourcesDao::insert)
        .subscribeOn(writeScheduler)
        .doOnSuccess { emit(Source.Operation.Add(adapter.fromLocalSource(localSource, it))) }
        .ignoreElement()

    private fun emit(operation: Source.Operation) {
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

}