package com.lelloman.read.feed

import com.lelloman.read.core.TimeProvider
import com.lelloman.read.core.di.qualifiers.IoScheduler
import com.lelloman.read.core.di.qualifiers.NewThreadScheduler
import com.lelloman.read.core.logger.LoggerFactory
import com.lelloman.read.http.HttpClient
import com.lelloman.read.persistence.db.ArticlesDao
import com.lelloman.read.persistence.db.SourcesDao
import com.lelloman.read.persistence.settings.AppSettings
import com.lelloman.read.utils.HtmlParser
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import javax.inject.Singleton

@Module
class FeedModule {

    @Singleton
    @Provides
    fun provideFeedRefresher(
        @IoScheduler ioScheduler: Scheduler,
        @NewThreadScheduler newThreadScheduler: Scheduler,
        sourcesDao: SourcesDao,
        articlesDao: ArticlesDao,
        timeProvider: TimeProvider,
        appSettings: AppSettings,
        loggerFactory: LoggerFactory,
        feedFetcher: FeedFetcher
    ): FeedRefresher = FeedRefresherImpl(
        ioScheduler = ioScheduler,
        newThreadScheduler = newThreadScheduler,
        sourcesDao = sourcesDao,
        articlesDao = articlesDao,
        timeProvider = timeProvider,
        appSettings = appSettings,
        loggerFactory = loggerFactory,
        feedFetcher = feedFetcher
    )

    @Singleton
    @Provides
    fun provideHtmlParser() = HtmlParser()

    @Provides
    fun provideFeedFetcher(
        httpClient: HttpClient,
        feedParser: FeedParser,
        htmlParser: HtmlParser,
        appSettings: AppSettings,
        meteredConnectionChecker: MeteredConnectionChecker
    ) = FeedFetcher(
        httpClient = httpClient,
        feedParser = feedParser,
        htmlParser = htmlParser,
        appSettings = appSettings,
        meteredConnectionChecker = meteredConnectionChecker
    )
}