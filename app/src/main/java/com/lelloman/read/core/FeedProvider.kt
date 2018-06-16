package com.lelloman.read.core

import com.lelloman.read.http.HttpClient
import com.lelloman.read.persistence.ArticlesDao
import com.lelloman.read.persistence.SourcesDao

class FeedProvider(
    private val httpClient: HttpClient,
    private val sourcesDao: SourcesDao,
    private val articlesDao: ArticlesDao
) {


}