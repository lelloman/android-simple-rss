package com.lelloman.read.sourceslist.repository

import com.lelloman.read.core.di.qualifiers.IoScheduler
import com.lelloman.read.sourceslist.model.Source
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SourcesRepository @Inject constructor(
    @IoScheduler private val ioScheduler: Scheduler
) {

    private val sources = mutableListOf(
        Source(1, "Source 1", "www.staceppa.com/feed.xml"),
        Source(2, "Ancora source", "http://www.porco2.xml")
    )

    private val sourcesSubject = PublishSubject.create<List<Source>>()
    private var isLoading = false

    fun fetchSources(): Observable<List<Source>> = sourcesSubject
        .hide()
        .doOnSubscribe { loadSource() }

    private fun loadSource() {
        if (isLoading) return

        isLoading = true

        Observable.just(sources)
            .subscribeOn(ioScheduler)
            .subscribe {
                sourcesSubject.onNext(it)
                isLoading = false
            }
    }
}