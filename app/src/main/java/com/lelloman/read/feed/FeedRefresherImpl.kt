package com.lelloman.read.feed

import com.lelloman.read.core.di.qualifiers.IoScheduler
import com.lelloman.read.core.di.qualifiers.NewThreadScheduler
import com.lelloman.read.http.HttpClient
import com.lelloman.read.http.HttpRequest
import com.lelloman.read.persistence.ArticlesDao
import com.lelloman.read.persistence.SourcesDao
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.subjects.BehaviorSubject

class FeedRefresherImpl(
    @IoScheduler private val ioScheduler: Scheduler,
    @NewThreadScheduler private val newThreadScheduler: Scheduler,
    private val httpClient: HttpClient,
    private val feedParser: FeedParser,
    private val sourcesDao: SourcesDao,
    private val articlesDao: ArticlesDao,
    private val mapper: ParsedFeedToArticleMapper
) : FeedRefresher {

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
            .flatMapMaybe { source ->
                httpClient
                    .request(HttpRequest(source.url))
                    .filter { it.isSuccessful }
                    .flatMap { feedParser.parseFeeds(it.body).toMaybe() }
                    .map {
                        it.map {
                            mapper.map(it, source)
                        }
                    }
                    .map { source to it }
                    .subscribeOn(newThreadScheduler)
            }
            .doAfterTerminate { isLoadingSubject.onNext(false) }
            .observeOn(ioScheduler)
            .subscribe { (source, articles) ->
                articlesDao.delete(source.id)
                articlesDao.insertAll(*articles.toTypedArray())
            }
    }
}