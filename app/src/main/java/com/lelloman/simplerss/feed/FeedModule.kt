package com.lelloman.simplerss.feed

import com.lelloman.common.di.qualifiers.IoScheduler
import com.lelloman.common.di.qualifiers.NewThreadScheduler
import com.lelloman.common.logger.LoggerFactory
import com.lelloman.common.utils.TimeProvider
import com.lelloman.common.utils.UrlValidator
import com.lelloman.common.view.MeteredConnectionChecker
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import javax.inject.Singleton

@Module
open class FeedModule {

    @Singleton
    @Provides
    fun provideFaviconBitmapProvider(
        loggerFactory: LoggerFactory
    ) = com.lelloman.simplerss.feed.FaviconBitmapProvider(
        loggerFactory = loggerFactory
    )

    @Singleton
    @Provides
    fun provideFeedRefresher(
        @IoScheduler ioScheduler: Scheduler,
        @NewThreadScheduler newThreadScheduler: Scheduler,
        @com.lelloman.simplerss.http.HttpPoolScheduler httpPoolScheduler: Scheduler,
        sourcesDao: com.lelloman.simplerss.persistence.db.SourcesDao,
        articlesDao: com.lelloman.simplerss.persistence.db.ArticlesDao,
        timeProvider: TimeProvider,
        appSettings: com.lelloman.simplerss.persistence.settings.AppSettings,
        loggerFactory: LoggerFactory,
        feedFetcher: com.lelloman.simplerss.feed.fetcher.FeedFetcher,
        faviconFetcher: com.lelloman.simplerss.feed.fetcher.FaviconFetcher
    ): com.lelloman.simplerss.feed.FeedRefresher = com.lelloman.simplerss.feed.FeedRefresherImpl(
        ioScheduler = ioScheduler,
        newThreadScheduler = newThreadScheduler,
        httpPoolScheduler = httpPoolScheduler,
        sourcesDao = sourcesDao,
        articlesDao = articlesDao,
        timeProvider = timeProvider,
        appSettings = appSettings,
        loggerFactory = loggerFactory,
        feedFetcher = feedFetcher,
        faviconFetcher = faviconFetcher
    )

    @Provides
    fun provideFeedFetcher(
        httpClient: com.lelloman.simplerss.http.HttpClient,
        feedParser: com.lelloman.simplerss.feed.FeedParser,
        htmlParser: com.lelloman.simplerss.html.HtmlParser,
        appSettings: com.lelloman.simplerss.persistence.settings.AppSettings,
        meteredConnectionChecker: MeteredConnectionChecker,
        loggerFactory: LoggerFactory
    ) = com.lelloman.simplerss.feed.fetcher.FeedFetcher(
        httpClient = httpClient,
        feedParser = feedParser,
        htmlParser = htmlParser,
        appSettings = appSettings,
        meteredConnectionChecker = meteredConnectionChecker,
        loggerFactory = loggerFactory
    )

    @Singleton
    @Provides
    fun provideFaviconFetcher(
        httpClient: com.lelloman.simplerss.http.HttpClient,
        urlValidator: UrlValidator
    ) = com.lelloman.simplerss.feed.fetcher.FaviconFetcher(
        httpClient = httpClient,
        urlValidator = urlValidator
    )

    @Provides
    fun provideFeedFinderHttpClient(
        httpClient: com.lelloman.simplerss.http.HttpClient,
        urlValidator: UrlValidator
    ) = com.lelloman.simplerss.feed.finder.FeedFinderHttpClient(
        httpClient = httpClient,
        urlValidator = urlValidator
    )

    @Provides
    fun provideFeedFinderParser(
        urlValidator: UrlValidator,
        htmlParser: com.lelloman.simplerss.html.HtmlParser,
        loggerFactory: LoggerFactory
    ) = com.lelloman.simplerss.feed.finder.FeedFinderParser(
        urlValidator = urlValidator,
        htmlParser = htmlParser,
        loggerFactory = loggerFactory
    )

    @Provides
    open fun provideFeedFinder(
        httpClient: com.lelloman.simplerss.feed.finder.FeedFinderHttpClient,
        parser: com.lelloman.simplerss.feed.finder.FeedFinderParser,
        feedFetcher: com.lelloman.simplerss.feed.fetcher.FeedFetcher,
        loggerFactory: LoggerFactory,
        @NewThreadScheduler newThreadScheduler: Scheduler
    ): com.lelloman.simplerss.feed.finder.FeedFinder = com.lelloman.simplerss.feed.finder.FeedFinderImpl(
        httpClient = httpClient,
        parser = parser,
        feedFetcher = feedFetcher,
        loggerFactory = loggerFactory,
        newThreadScheduler = newThreadScheduler
    )
}