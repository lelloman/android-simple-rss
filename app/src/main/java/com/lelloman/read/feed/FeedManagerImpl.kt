package com.lelloman.read.feed

import com.lelloman.read.http.HttpClient
import com.lelloman.read.persistence.ArticlesDao
import com.lelloman.read.persistence.SourcesDao
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class FeedManagerImpl(
    private val httpClient: HttpClient,
    private val feedParser: FeedParser,
    private val sourcesDao: SourcesDao,
    private val articlesDao: ArticlesDao
) : FeedManager {

    private val isLoadingSubject = BehaviorSubject.create<Boolean>()
    override val isLoading: Observable<Boolean> = isLoadingSubject
        .hide()
        .distinctUntilChanged()

    init {
        isLoadingSubject.onNext(false)
    }

    @Synchronized
    override fun refresh() {
        if (isLoadingSubject.value == true) {
            return
        }
        isLoadingSubject.onNext(true)

        sourcesDao
            .getActiveSources()
            .firstOrError()
            .flatMapObservable { Observable.fromIterable(it) }
            .doAfterTerminate { isLoadingSubject.onNext(false) }
            .subscribe {

            }
    }
}