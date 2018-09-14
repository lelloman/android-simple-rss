package com.lelloman.read.feed

import com.lelloman.common.utils.TimeProvider
import com.lelloman.common.utils.UrlValidator
import com.lelloman.common.view.MeteredConnectionChecker
import com.lelloman.read.core.di.qualifiers.IoScheduler
import com.lelloman.read.core.di.qualifiers.NewThreadScheduler
import com.lelloman.read.core.logger.LoggerFactory
import com.lelloman.read.feed.fetcher.FaviconFetcher
import com.lelloman.read.feed.fetcher.FeedFetcher
import com.lelloman.read.feed.finder.FeedFinder
import com.lelloman.read.feed.finder.FeedFinderHttpClient
import com.lelloman.read.feed.finder.FeedFinderParser
import com.lelloman.read.html.HtmlParser
import com.lelloman.read.http.HttpClient
import com.lelloman.read.persistence.db.ArticlesDao
import com.lelloman.read.persistence.db.SourcesDao
import com.lelloman.read.persistence.settings.AppSettings
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
        feedFetcher: FeedFetcher,
        faviconFetcher: FaviconFetcher
    ): FeedRefresher = FeedRefresherImpl(
        ioScheduler = ioScheduler,
        newThreadScheduler = newThreadScheduler,
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
    fun provideFeedFinder(
        httpClient: FeedFinderHttpClient,
        parser: FeedFinderParser,
        feedFetcher: FeedFetcher,
        loggerFactory: LoggerFactory,
        @NewThreadScheduler newThreadScheduler: Scheduler
    ) = FeedFinder(
        httpClient = httpClient,
        parser = parser,
        feedFetcher = feedFetcher,
        loggerFactory = loggerFactory,
        newThreadScheduler = newThreadScheduler
    )
}