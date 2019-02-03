package com.lelloman.simplerss.feed

import com.lelloman.common.logger.LoggerFactory
import com.lelloman.common.utils.TimeProvider
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject

class FeedRefresherImpl(
    private val ioScheduler: Scheduler,
    private val newThreadScheduler: Scheduler,
    private val httpPoolScheduler: Scheduler,
    private val sourcesDao: com.lelloman.simplerss.persistence.db.SourcesDao,
    private val articlesDao: com.lelloman.simplerss.persistence.db.ArticlesDao,
    private val timeProvider: TimeProvider,
    private val appSettings: com.lelloman.simplerss.persistence.settings.AppSettings,
    private val faviconFetcher: com.lelloman.simplerss.feed.fetcher.FaviconFetcher,
    loggerFactory: LoggerFactory,
    private val feedFetcher: com.lelloman.simplerss.feed.fetcher.FeedFetcher
) : com.lelloman.simplerss.feed.FeedRefresher {

    private val isLoadingSubject = BehaviorSubject.create<Boolean>()
    override val isLoading: Observable<Boolean> = isLoadingSubject
        .hide()
        .distinctUntilChanged()

    private val logger = loggerFactory.getLogger(javaClass)

    init {
        isLoadingSubject.onNext(false)
    }

    @Synchronized
    override fun refresh() {
        if (isLoadingSubject.value == true) {
            return
        }
        isLoadingSubject.onNext(true)

        @Suppress("UNUSED_VARIABLE")
        val ignored = Single
            .zip(
                sourcesDao
                    .getActiveSources()
                    .firstOrError(),
                appSettings
                    .sourceRefreshMinInterval
                    .firstOrError(),
                BiFunction<List<com.lelloman.simplerss.persistence.db.model.Source>, com.lelloman.simplerss.persistence.settings.SourceRefreshInterval, Pair<List<com.lelloman.simplerss.persistence.db.model.Source>, com.lelloman.simplerss.persistence.settings.SourceRefreshInterval>> { sources, minRefreshInterval ->
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
                    .subscribeOn(httpPoolScheduler)
                    .onErrorComplete()
            }
            .doAfterTerminate { isLoadingSubject.onNext(false) }
            .observeOn(ioScheduler)
            .subscribe({ (source, articles) ->
                articlesDao.deleteArticlesFromSource(source.id)
                articlesDao.insertAll(*articles.toTypedArray())
                sourcesDao.updateSourceLastFetched(source.id, timeProvider.nowUtcMs())
            }, {
                logger.w("Something went wrong in refresh subscription", it)
            })

        sourcesDao
            .getAll()
            .flatMap { Flowable.fromIterable(it) }
            .filter { it.favicon == null }
            .flatMapMaybe { source ->
                faviconFetcher
                    .getPngFavicon(source.url)
                    .map { source to it }
                    .subscribeOn(httpPoolScheduler)
                    .onErrorComplete()
            }
            .flatMapCompletable { (source, pngBytes) ->
                Completable.fromAction {
                    source.favicon = pngBytes
                    sourcesDao.updateSource(source)
                }
            }
            .subscribeOn(newThreadScheduler)
            .subscribe()
    }
}