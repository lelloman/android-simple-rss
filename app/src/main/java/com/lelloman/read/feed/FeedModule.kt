package com.lelloman.read.feed

import com.lelloman.read.core.TimeProvider
import com.lelloman.read.core.di.qualifiers.IoScheduler
import com.lelloman.read.core.di.qualifiers.NewThreadScheduler
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
        httpClient: HttpClient,
        feedParser: FeedParser,
        sourcesDao: SourcesDao,
        articlesDao: ArticlesDao,
        htmlParser: HtmlParser,
        timeProvider: TimeProvider,
        appSettings: AppSettings
    ): FeedRefresher = FeedRefresherImpl(
        ioScheduler = ioScheduler,
        newThreadScheduler = newThreadScheduler,
        httpClient = httpClient,
        feedParser = feedParser,
        sourcesDao = sourcesDao,
        articlesDao = articlesDao,
        htmlParser = htmlParser,
        timeProvider = timeProvider,
        appSettings = appSettings
    )

    @Singleton
    @Provides
    fun provideHtmlParser() = HtmlParser()
}