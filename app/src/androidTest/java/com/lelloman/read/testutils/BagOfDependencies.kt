package com.lelloman.read.testutils

import android.support.test.InstrumentationRegistry
import com.lelloman.common.logger.LoggerFactoryImpl
import com.lelloman.common.settings.BaseApplicationSettings
import com.lelloman.common.settings.BaseApplicationSettingsImpl
import com.lelloman.common.utils.TimeProvider
import com.lelloman.common.utils.TimeProviderImpl
import com.lelloman.common.utils.UrlValidatorImpl
import com.lelloman.common.view.MeteredConnectionChecker
import com.lelloman.common.view.MeteredConnectionCheckerImpl
import com.lelloman.read.feed.FeedParser
import com.lelloman.read.feed.fetcher.FeedFetcher
import com.lelloman.read.feed.finder.FeedFinder
import com.lelloman.read.feed.finder.FeedFinderHttpClient
import com.lelloman.read.feed.finder.FeedFinderParser
import com.lelloman.read.html.HtmlParser
import com.lelloman.read.http.HttpClientImpl
import com.lelloman.read.persistence.settings.AppSettings
import com.lelloman.read.persistence.settings.AppSettingsImpl
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient

class BagOfDependencies {
    val timeProvider: TimeProvider
    val htmlParser: HtmlParser
    val meteredConnectionChecker: MeteredConnectionChecker
    val httpClient: HttpClientImpl
    val feedFinder: FeedFinder
    val feedParser: FeedParser
    val baseAppSettings: BaseApplicationSettings
    val appSettings: AppSettings

    init {
        val okHttpClient = OkHttpClient.Builder().build()
        val loggerFactory = LoggerFactoryImpl()
        timeProvider = TimeProviderImpl()
        htmlParser = HtmlParser()
        val urlValidator = UrlValidatorImpl()

        val targetContext = InstrumentationRegistry.getTargetContext()
        meteredConnectionChecker = MeteredConnectionCheckerImpl(targetContext)

        baseAppSettings = BaseApplicationSettingsImpl(targetContext)
        appSettings = AppSettingsImpl(targetContext, baseAppSettings)

        feedParser = FeedParser(timeProvider)

        httpClient = HttpClientImpl(
            okHttpClient = okHttpClient,
            loggerFactory = loggerFactory,
            timeProvider = timeProvider
        )

        val feedFetcher = FeedFetcher(
            httpClient = httpClient,
            feedParser = feedParser,
            htmlParser = htmlParser,
            meteredConnectionChecker = meteredConnectionChecker,
            appSettings = appSettings,
            loggerFactory = loggerFactory
        )

        val feedFinderHttpClient = FeedFinderHttpClient(
            httpClient = httpClient,
            urlValidator = urlValidator
        )

        val feedFinderParser = FeedFinderParser(
            urlValidator = urlValidator,
            htmlParser = htmlParser,
            loggerFactory = loggerFactory
        )

        feedFinder = FeedFinder(
            httpClient = feedFinderHttpClient,
            parser = feedFinderParser,
            feedFetcher = feedFetcher,
            loggerFactory = loggerFactory,
            newThreadScheduler = Schedulers.newThread()
        )
    }
}