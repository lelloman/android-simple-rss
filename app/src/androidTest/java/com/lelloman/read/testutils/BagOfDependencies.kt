package com.lelloman.read.testutils

import android.support.test.InstrumentationRegistry
import com.lelloman.read.core.MeteredConnectionChecker
import com.lelloman.read.core.TimeProvider
import com.lelloman.read.core.logger.LoggerFactoryImpl
import com.lelloman.read.feed.FeedFetcher
import com.lelloman.read.feed.FeedFinder
import com.lelloman.read.feed.FeedParser
import com.lelloman.read.html.HtmlParser
import com.lelloman.read.http.HttpClientImpl
import com.lelloman.read.persistence.settings.AppSettings
import com.lelloman.read.persistence.settings.AppSettingsImpl
import com.lelloman.read.utils.UrlValidator
import okhttp3.OkHttpClient

class BagOfDependencies {
    val timeProvider: TimeProvider
    val htmlParser: HtmlParser
    val meteredConnectionChecker: MeteredConnectionChecker
    val httpClient: HttpClientImpl
    val feedFinder: FeedFinder
    val feedParser: FeedParser
    val appSettings: AppSettings

    init {
        val okHttpClient = OkHttpClient.Builder().build()
        val loggerFactory = LoggerFactoryImpl()
        timeProvider = TimeProvider()
        htmlParser = HtmlParser()
        val urlValidator = UrlValidator()

        val targetContext = InstrumentationRegistry.getTargetContext()
        meteredConnectionChecker = MeteredConnectionChecker(targetContext)
        appSettings = AppSettingsImpl(targetContext)

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
            appSettings = appSettings
        )

        feedFinder = FeedFinder(
            httpClient = httpClient,
            urlValidator = urlValidator,
            htmlParser = htmlParser,
            feedFetcher = feedFetcher
        )
    }
}