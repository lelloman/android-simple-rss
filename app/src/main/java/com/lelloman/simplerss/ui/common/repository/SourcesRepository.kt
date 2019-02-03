package com.lelloman.simplerss.ui.common.repository

import com.lelloman.common.di.qualifiers.IoScheduler
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SourcesRepository @Inject constructor(
    @IoScheduler private val ioScheduler: Scheduler,
    private val sourcesDao: com.lelloman.simplerss.persistence.db.SourcesDao,
    private val feedRefresher: com.lelloman.simplerss.feed.FeedRefresher,
    private val articlesDao: com.lelloman.simplerss.persistence.db.ArticlesDao
) {

    private val sourcesSubject = PublishSubject.create<List<com.lelloman.simplerss.persistence.db.model.Source>>()
    private var isLoading = false

    fun fetchSources(): Observable<List<com.lelloman.simplerss.persistence.db.model.Source>> = sourcesSubject
        .hide()
        .doOnSubscribe { loadSource() }

    fun insertSource(source: com.lelloman.simplerss.persistence.db.model.Source): Single<Long> = Single.fromCallable {
        val id = sourcesDao.insert(source)
        feedRefresher.refresh()
        id
    }

    fun deleteSource(source: com.lelloman.simplerss.persistence.db.model.Source): Single<com.lelloman.simplerss.ui.common.repository.DeletedSource> = articlesDao
        .getAllFromSource(source.id)
        .firstOrError()
        .map { com.lelloman.simplerss.ui.common.repository.DeletedSource(source, it) }
        .flatMap {
            Single.fromCallable {
                sourcesDao.delete(source.id)
                it
            }
        }

    fun getSource(sourceId: Long) = sourcesDao.getSource(sourceId)

    fun setSourceIsActive(sourceId: Long, isActive: Boolean): Completable = Completable.fromCallable {
        sourcesDao.setSourceIsActive(sourceId, isActive)
        if (isActive) {
            feedRefresher.refresh()
        }
    }

    private fun loadSource() {
        if (isLoading) return

        isLoading = true

        @Suppress("UNUSED_VARIABLE")
        val unused = sourcesDao.getAll()
            .subscribeOn(ioScheduler)
            .subscribe {
                sourcesSubject.onNext(it)
                isLoading = false
            }
    }
}