package com.lelloman.read.feed

import com.lelloman.read.core.di.qualifiers.IoScheduler
import com.lelloman.read.core.di.qualifiers.NewThreadScheduler
import com.lelloman.read.http.HttpClient
import com.lelloman.read.persistence.ArticlesDao
import com.lelloman.read.persistence.SourcesDao
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
        htmlParser: HtmlParser
    ): FeedRefresher = FeedRefresherImpl(
        ioScheduler = ioScheduler,
        newThreadScheduler = newThreadScheduler,
        httpClient = httpClient,
        feedParser = feedParser,
        sourcesDao = sourcesDao,
        articlesDao = articlesDao,
        htmlParser = htmlParser
    )

    @Singleton
    @Provides
    fun provideHtmlParser() = HtmlParser()
}