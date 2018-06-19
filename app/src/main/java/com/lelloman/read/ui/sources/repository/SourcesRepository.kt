package com.lelloman.read.ui.sources.repository

import com.lelloman.read.core.di.qualifiers.IoScheduler
import com.lelloman.read.persistence.db.ArticlesDao
import com.lelloman.read.persistence.db.SourcesDao
import com.lelloman.read.persistence.db.model.Source
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
    private val sourcesDao: SourcesDao,
    private val articlesDao: ArticlesDao
) {

    private val sourcesSubject = PublishSubject.create<List<Source>>()
    private var isLoading = false

    fun fetchSources(): Observable<List<Source>> = sourcesSubject
        .hide()
        .doOnSubscribe { loadSource() }

    fun insertSource(source: Source): Single<Long> = Single.fromCallable { sourcesDao.insert(source) }

    fun getSource(sourceId: Long) = sourcesDao.getSource(sourceId)

    fun setSourceIsActive(sourceId: Long, isActive: Boolean): Completable = Completable
        .fromCallable { sourcesDao.setSourceIsActive(sourceId, isActive) }
        .andThen(
            if (isActive) {
                Completable.complete()
            } else {
                Completable
                    .fromAction {
                        articlesDao.deleteArticlesFromSource(sourceId)
                        sourcesDao.updateSourceLastFetched(sourceId, 0L)
                    }

            }
        )


    private fun loadSource() {
        if (isLoading) return

        isLoading = true

        sourcesDao.getAll()
            .subscribeOn(ioScheduler)
            .subscribe {
                sourcesSubject.onNext(it)
                isLoading = false
            }
    }
}