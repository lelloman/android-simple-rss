package com.lelloman.read.sources.repository

import com.lelloman.read.core.di.qualifiers.IoScheduler
import com.lelloman.read.persistence.SourcesDao
import com.lelloman.read.persistence.model.Source
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SourcesRepository @Inject constructor(
    @IoScheduler private val ioScheduler: Scheduler,
    private val dao: SourcesDao
) {

    private val sourcesSubject = PublishSubject.create<List<Source>>()
    private var isLoading = false

    fun fetchSources(): Observable<List<Source>> = sourcesSubject
        .hide()
        .doOnSubscribe { loadSource() }

    fun insertSource(source: Source): Single<Long> = Single.fromCallable { dao.insert(source) }

    private fun loadSource() {
        if (isLoading) return

        isLoading = true

        dao.getAll()
            .subscribeOn(ioScheduler)
            .subscribe {
                sourcesSubject.onNext(it)
                isLoading = false
            }
    }
}