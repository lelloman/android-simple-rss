package com.lelloman.read.feed

import com.lelloman.read.core.di.qualifiers.IoScheduler
import com.lelloman.read.core.di.qualifiers.NewThreadScheduler
import com.lelloman.read.http.HttpClient
import com.lelloman.read.persistence.ArticlesDao
import com.lelloman.read.persistence.SourcesDao
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import javax.inject.Singleton

@Module
class FeedModule {

    @Singleton
    @Provides
    fun provideFeedManager(
        @IoScheduler ioScheduler: Scheduler,
        @NewThreadScheduler newThreadScheduler: Scheduler,
        httpClient: HttpClient,
        feedParser: FeedParser,
        sourcesDao: SourcesDao,
        articlesDao: ArticlesDao
    ): FeedManager = FeedManagerImpl(
        ioScheduler = ioScheduler,
        newThreadScheduler = newThreadScheduler,
        httpClient = httpClient,
        feedParser = feedParser,
        sourcesDao = sourcesDao,
        articlesDao = articlesDao
    )
}