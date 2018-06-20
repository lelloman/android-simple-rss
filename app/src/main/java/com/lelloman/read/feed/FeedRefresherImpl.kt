package com.lelloman.read.feed

import com.lelloman.read.core.TimeProvider
import com.lelloman.read.core.di.qualifiers.IoScheduler
import com.lelloman.read.core.di.qualifiers.NewThreadScheduler
import com.lelloman.read.http.HttpClient
import com.lelloman.read.http.HttpRequest
import com.lelloman.read.persistence.db.ArticlesDao
import com.lelloman.read.persistence.db.SourcesDao
import com.lelloman.read.persistence.db.model.Article
import com.lelloman.read.persistence.db.model.Source
import com.lelloman.read.persistence.settings.AppSettings
import com.lelloman.read.utils.HtmlParser
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
    private val htmlParser: HtmlParser,
    private val timeProvider: TimeProvider,
    private val appSettings: AppSettings
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
            .filter(::isSourceStale)
            .flatMapMaybe { source ->
                httpClient
                    .request(HttpRequest(source.url))
                    .filter { it.isSuccessful }
                    .flatMap { feedParser.parseFeeds(it.body).toMaybe() }
                    .map {
                        it.map {
                            parsedFeedToArticle(source, it)
                        }
                    }
                    .map { source to it }
                    .subscribeOn(newThreadScheduler)
            }
            .doAfterTerminate { isLoadingSubject.onNext(false) }
            .observeOn(ioScheduler)
            .subscribe { (source, articles) ->
                articlesDao.deleteArticlesFromSource(source.id)
                articlesDao.insertAll(*articles.toTypedArray())
                sourcesDao.updateSourceLastFetched(source.id, timeProvider.nowUtcMs())
            }
    }

    private fun isSourceStale(source: Source) =
        (timeProvider.nowUtcMs() - source.lastFetched) > appSettings.sourceRefreshMinInterval.ms

    private fun parsedFeedToArticle(source: Source, parsedFeed: ParsedFeed): Article {
        val (title, imagesUrl1) = htmlParser.withHtml(parsedFeed.title)
        val (subtitle, imagesUrl2) = htmlParser.withHtml(parsedFeed.subtitle)

        val imageUrl = when {
            imagesUrl1.isNotEmpty() -> imagesUrl1[0]
            imagesUrl2.isNotEmpty() -> imagesUrl2[0]
            else -> null
        }

        return Article(
            id = 0L,
            title = title,
            subtitle = subtitle,
            time = parsedFeed.timestamp,
            link = parsedFeed.link,
            content = "",
            sourceName = source.name,
            sourceId = source.id,
            imageUrl = imageUrl
        )
    }
}