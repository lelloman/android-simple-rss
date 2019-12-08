package com.lelloman.simplerss.di

import com.lelloman.common.di.KoinModuleFactory
import com.lelloman.common.di.qualifiers.IoScheduler
import com.lelloman.common.di.qualifiers.NewThreadScheduler
import com.lelloman.common.http.HttpClient
import com.lelloman.common.http.HttpPoolScheduler
import com.lelloman.common.logger.LoggerFactory
import com.lelloman.common.utils.TimeProvider
import com.lelloman.common.utils.UrlValidator
import com.lelloman.common.view.MeteredConnectionChecker
import com.lelloman.simplerss.feed.*
import com.lelloman.simplerss.feed.fetcher.BitmapDecoder
import com.lelloman.simplerss.feed.fetcher.FaviconFetcher
import com.lelloman.simplerss.feed.fetcher.FeedFetcher
import com.lelloman.simplerss.feed.finder.FeedFinder
import com.lelloman.simplerss.feed.finder.FeedFinderHttpClient
import com.lelloman.simplerss.feed.finder.FeedFinderImpl
import com.lelloman.simplerss.feed.finder.FeedFinderParser
import com.lelloman.simplerss.html.HtmlParser
import com.lelloman.simplerss.persistence.db.ArticlesDao
import com.lelloman.simplerss.persistence.db.SourcesDao
import com.lelloman.simplerss.persistence.settings.AppSettings
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import org.koin.dsl.module
import java.util.concurrent.Executors

open class FeedModuleFactory : KoinModuleFactory {

    override fun makeKoinModule(override: Boolean) = module(override=override) {
        single {
            provideFaviconBitmapProvider(loggerFactory = get())
        }
        single {
            provideFeedRefresher(
                ioScheduler = get(IoScheduler),
                newThreadScheduler = get(NewThreadScheduler),
                httpPoolScheduler = get(HttpPoolScheduler),
                sourcesDao = get(),
                articlesDao = get(),
                timeProvider = get(),
                appSettings = get(),
                loggerFactory = get(),
                feedFetcher = get(),
                faviconFetcher = get()
            )
        }
        factory {
            provideFeedFetcher(
                htmlParser = get(),
                httpClient = get(),
                feedParser = get(),
                appSettings = get(),
                meteredConnectionChecker = get(),
                loggerFactory = get()
            )
        }
        single {
            provideBitmapDecoder()
        }
        single {
            provideFaviconFetcher(
                httpClient = get(),
                urlValidator = get(),
                loggerFactory = get(),
                bitmapDecoder = get()
            )
        }
        factory {
            provideFeedFinderHttpClient(
                httpClient = get(),
                urlValidator = get()
            )
        }
        factory {
            provideFeedFinderParser(
                urlValidator = get(),
                htmlParser = get(),
                loggerFactory = get()
            )
        }
        factory {
            provideFeedFinder(
                httpClient = get(),
                parser = get(),
                feedFetcher = get(),
                loggerFactory = get(),
                scheduler = get(FeedFinderPoolScheduler)
            )
        }
        single(FeedFinderPoolScheduler) {
            provideFeedFinderPoolScheduler()
        }
        factory {
            provideFeedParser(timeProvider = get())
        }
    }

    protected open fun provideFaviconBitmapProvider(
        loggerFactory: LoggerFactory
    ) = FaviconBitmapProvider(
        loggerFactory = loggerFactory
    )

    protected open fun provideFeedRefresher(
        ioScheduler: Scheduler,
        newThreadScheduler: Scheduler,
        httpPoolScheduler: Scheduler,
        sourcesDao: SourcesDao,
        articlesDao: ArticlesDao,
        timeProvider: TimeProvider,
        appSettings: AppSettings,
        loggerFactory: LoggerFactory,
        feedFetcher: FeedFetcher,
        faviconFetcher: FaviconFetcher
    ): FeedRefresher =
        FeedRefresherImpl(
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

    protected open fun provideFeedFetcher(
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

    protected open fun provideBitmapDecoder() = BitmapDecoder()

    protected open fun provideFaviconFetcher(
        httpClient: HttpClient,
        urlValidator: UrlValidator,
        loggerFactory: LoggerFactory,
        bitmapDecoder: BitmapDecoder
    ) = FaviconFetcher(
        httpClient = httpClient,
        urlValidator = urlValidator,
        loggerFactory = loggerFactory,
        bitmapDecoder = bitmapDecoder
    )

    protected open fun provideFeedFinderHttpClient(
        httpClient: HttpClient,
        urlValidator: UrlValidator
    ) = FeedFinderHttpClient(
        httpClient = httpClient,
        urlValidator = urlValidator
    )

    protected open fun provideFeedFinderParser(
        urlValidator: UrlValidator,
        htmlParser: HtmlParser,
        loggerFactory: LoggerFactory
    ) = FeedFinderParser(
        urlValidator = urlValidator,
        htmlParser = htmlParser,
        loggerFactory = loggerFactory
    )

    protected open fun provideFeedFinder(
        httpClient: FeedFinderHttpClient,
        parser: FeedFinderParser,
        feedFetcher: FeedFetcher,
        loggerFactory: LoggerFactory,
        scheduler: Scheduler
    ): FeedFinder = FeedFinderImpl(
        httpClient = httpClient,
        parser = parser,
        feedFetcher = feedFetcher,
        loggerFactory = loggerFactory,
        scheduler = scheduler
    )

    protected open fun provideFeedParser(
        timeProvider: TimeProvider
    ) = FeedParser(timeProvider = timeProvider)

    protected open fun provideFeedFinderPoolScheduler(): Scheduler = Executors
        .newFixedThreadPool(8)
        .let(Schedulers::from)
}