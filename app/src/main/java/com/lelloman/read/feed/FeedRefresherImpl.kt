package com.lelloman.read.feed

import com.lelloman.read.core.TimeProvider
import com.lelloman.read.core.di.qualifiers.IoScheduler
import com.lelloman.read.core.di.qualifiers.NewThreadScheduler
import com.lelloman.read.core.logger.LoggerFactory
import com.lelloman.read.persistence.db.ArticlesDao
import com.lelloman.read.persistence.db.SourcesDao
import com.lelloman.read.persistence.db.model.Source
import com.lelloman.read.persistence.settings.AppSettings
import com.lelloman.read.persistence.settings.SourceRefreshInterval
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject

class FeedRefresherImpl(
    @IoScheduler private val ioScheduler: Scheduler,
    @NewThreadScheduler private val newThreadScheduler: Scheduler,
    private val sourcesDao: SourcesDao,
    private val articlesDao: ArticlesDao,
    private val timeProvider: TimeProvider,
    private val appSettings: AppSettings,
    loggerFactory: LoggerFactory,
    private val feedFetcher: FeedFetcher
) : FeedRefresher {

    private val isLoadingSubject = BehaviorSubject.create<Boolean>()
    override val isLoading: Observable<Boolean> = isLoadingSubject
        .hide()
        .distinctUntilChanged()

    private val logger = loggerFactory.getLogger(javaClass.simpleName)

    init {
        isLoadingSubject.onNext(false)
    }

    @Synchronized
    override fun refresh() {
        if (isLoadingSubject.value == true) {
            return
        }
        isLoadingSubject.onNext(true)

        Single
            .zip(
                sourcesDao
                    .getActiveSources()
                    .firstOrError(),
                appSettings
                    .sourceRefreshMinInterval
                    .firstOrError(),
                BiFunction<List<Source>, SourceRefreshInterval, Pair<List<Source>, SourceRefreshInterval>> { sources, minRefreshInterval ->
                    sources to minRefreshInterval
                }
            )
            .flatMapObservable { (sources, interval) ->
                Observable.fromIterable(sources.map { it to interval })
            }
            .filter { (source, minRefreshInterval) ->
                timeProvider.nowUtcMs() - source.lastFetched > minRefreshInterval.ms
            }
            .map { (source, _) -> source }
            .flatMapMaybe { source ->
                feedFetcher.fetchFeed(source)
                    .subscribeOn(newThreadScheduler)
                    .onErrorComplete()
            }
            .doAfterTerminate { isLoadingSubject.onNext(false) }
            .observeOn(ioScheduler)
            .subscribe({ (source, articles) ->
                articlesDao.deleteArticlesFromSource(source.id)
                articlesDao.insertAll(*articles.toTypedArray())
                sourcesDao.updateSourceLastFetched(source.id, timeProvider.nowUtcMs())
            }, {
                logger.e("Something went wrong in refresh subscription", it)
            })
    }
}