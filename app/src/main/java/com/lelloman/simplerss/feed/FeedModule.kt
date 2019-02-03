package com.lelloman.simplerss.feed

import com.lelloman.common.di.qualifiers.IoScheduler
import com.lelloman.common.di.qualifiers.NewThreadScheduler
import com.lelloman.common.logger.LoggerFactory
import com.lelloman.common.utils.TimeProvider
import com.lelloman.common.utils.UrlValidator
import com.lelloman.common.view.MeteredConnectionChecker
import com.lelloman.simplerss.feed.fetcher.FaviconFetcher
import com.lelloman.simplerss.feed.fetcher.FeedFetcher
import com.lelloman.simplerss.feed.finder.FeedFinder
import com.lelloman.simplerss.feed.finder.FeedFinderHttpClient
import com.lelloman.simplerss.feed.finder.FeedFinderImpl
import com.lelloman.simplerss.feed.finder.FeedFinderParser
import com.lelloman.simplerss.html.HtmlParser
import com.lelloman.simplerss.http.HttpClient
import com.lelloman.simplerss.http.HttpPoolScheduler
import com.lelloman.simplerss.persistence.db.ArticlesDao
import com.lelloman.simplerss.persistence.db.SourcesDao
import com.lelloman.simplerss.persistence.settings.AppSettings
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
    ) = FaviconBitmapProvider(
        loggerFactory = loggerFactory
    )

    @Singleton
    @Provides
    fun provideFeedRefresher(
        @IoScheduler ioScheduler: Scheduler,
        @NewThreadScheduler newThreadScheduler: Scheduler,
        @HttpPoolScheduler httpPoolScheduler: Scheduler,
        sourcesDao: SourcesDao,
        articlesDao: ArticlesDao,
        timeProvider: TimeProvider,
        appSettings: AppSettings,
        loggerFactory: LoggerFactory,
        feedFetcher: FeedFetcher,
        faviconFetcher: FaviconFetcher
    ): FeedRefresher = FeedRefresherImpl(
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
        httpClient: HttpClient,
        feedParser: FeedParser,
        htmlParser: HtmlParser,
        appSettings: AppSettings,
        meteredConnectionChecker: MeteredConnectionChecker,
        loggerFactory: LoggerFactory
    ) = FeedFetcher(
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
        httpClient: HttpClient,
        urlValidator: UrlValidator
    ) = FaviconFetcher(
        httpClient = httpClient,
        urlValidator = urlValidator
    )

    @Provides
    fun provideFeedFinderHttpClient(
        httpClient: HttpClient,
        urlValidator: UrlValidator
    ) = FeedFinderHttpClient(
        httpClient = httpClient,
        urlValidator = urlValidator
    )

    @Provides
    fun provideFeedFinderParser(
        urlValidator: UrlValidator,
        htmlParser: HtmlParser,
        loggerFactory: LoggerFactory
    ) = FeedFinderParser(
        urlValidator = urlValidator,
        htmlParser = htmlParser,
        loggerFactory = loggerFactory
    )

    @Provides
    open fun provideFeedFinder(
        httpClient: FeedFinderHttpClient,
        parser: FeedFinderParser,
        feedFetcher: FeedFetcher,
        loggerFactory: LoggerFactory,
        @NewThreadScheduler newThreadScheduler: Scheduler
    ): FeedFinder = FeedFinderImpl(
        httpClient = httpClient,
        parser = parser,
        feedFetcher = feedFetcher,
        loggerFactory = loggerFactory,
        newThreadScheduler = newThreadScheduler
    )
}