package com.lelloman.simplerss.testutils

import android.app.Application
import android.support.test.InstrumentationRegistry
import com.lelloman.common.di.BaseApplicationModule
import com.lelloman.common.settings.BaseApplicationSettings
import com.lelloman.common.settings.BaseSettingsModule
import com.lelloman.common.utils.TimeProvider
import com.lelloman.common.utils.TimeProviderImpl
import com.lelloman.common.utils.UrlValidatorImpl
import com.lelloman.common.view.MeteredConnectionChecker
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient

class BagOfDependencies {
    private val timeProvider: TimeProvider
    private val htmlParser: com.lelloman.simplerss.html.HtmlParser
    private val meteredConnectionChecker: MeteredConnectionChecker
    private val httpClient: com.lelloman.simplerss.http.HttpClientImpl
    val feedFinder: com.lelloman.simplerss.feed.finder.FeedFinder
    private val feedParser: com.lelloman.simplerss.feed.FeedParser
    private val baseAppSettings: BaseApplicationSettings
    private val appSettings: com.lelloman.simplerss.persistence.settings.AppSettings

    init {
        val okHttpClient = OkHttpClient.Builder().build()
        val targetContext = InstrumentationRegistry.getTargetContext()
        val baseAppModule = BaseApplicationModule(targetContext as Application)
        val loggerFactory = baseAppModule.provideLoggerFactory()
        timeProvider = TimeProviderImpl()
        htmlParser = com.lelloman.simplerss.html.HtmlParser()
        val urlValidator = UrlValidatorImpl()

        meteredConnectionChecker = baseAppModule.provideMeteredConnectionChecker(targetContext)

        baseAppSettings = BaseSettingsModule().provideBaseApplicationSettings(targetContext)
        appSettings = com.lelloman.simplerss.persistence.settings.AppSettingsImpl(targetContext, baseAppSettings)

        feedParser = com.lelloman.simplerss.feed.FeedParser(timeProvider)

        httpClient = com.lelloman.simplerss.http.HttpClientImpl(
            okHttpClient = okHttpClient,
            loggerFactory = loggerFactory,
            timeProvider = timeProvider
        )

        val feedFetcher = com.lelloman.simplerss.feed.fetcher.FeedFetcher(
            httpClient = httpClient,
            feedParser = feedParser,
            htmlParser = htmlParser,
            meteredConnectionChecker = meteredConnectionChecker,
            appSettings = appSettings,
            loggerFactory = loggerFactory
        )

        val feedFinderHttpClient = com.lelloman.simplerss.feed.finder.FeedFinderHttpClient(
            httpClient = httpClient,
            urlValidator = urlValidator
        )

        val feedFinderParser = com.lelloman.simplerss.feed.finder.FeedFinderParser(
            urlValidator = urlValidator,
            htmlParser = htmlParser,
            loggerFactory = loggerFactory
        )

        feedFinder = com.lelloman.simplerss.feed.finder.FeedFinderImpl(
            httpClient = feedFinderHttpClient,
            parser = feedFinderParser,
            feedFetcher = feedFetcher,
            loggerFactory = loggerFactory,
            newThreadScheduler = Schedulers.newThread()
        )
    }
}