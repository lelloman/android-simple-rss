package com.lelloman.read.feed

import com.lelloman.read.http.HttpClient
import com.lelloman.read.persistence.ArticlesDao
import com.lelloman.read.persistence.SourcesDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FeedModule {

    @Singleton
    @Provides
    fun provideFeedManager(
        httpClient: HttpClient,
        feedParser: FeedParser,
        sourcesDao: SourcesDao,
        articlesDao: ArticlesDao
    ): FeedManager = FeedManagerImpl(
        httpClient = httpClient,
        feedParser = feedParser,
        sourcesDao = sourcesDao,
        articlesDao = articlesDao
    )
}