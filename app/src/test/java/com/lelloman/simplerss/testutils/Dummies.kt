package com.lelloman.simplerss.testutils

import com.lelloman.simplerss.feed.finder.FoundFeed
import com.lelloman.simplerss.persistence.db.model.Article
import com.lelloman.simplerss.persistence.db.model.Source
import com.lelloman.simplerss.persistence.db.model.SourceArticle

fun dummyArticle(index: Int = 1) = Article(
    id = index.toLong(),
    title = "article $index",
    subtitle = "subtitle $index",
    content = "content $index",
    link = "link $index",
    imageUrl = "image url $index",
    time = index.toLong(),
    sourceId = index.toLong()
)

fun dummySourceArticle(index: Int = 1) = SourceArticle(
    id = index.toLong(),
    title = "article $index",
    subtitle = "subtitle $index",
    content = "content $index",
    link = "link $index",
    imageUrl = "image url $index",
    time = index.toLong(),
    sourceId = index.toLong(),
    sourceName = "sourceName $index",
    favicon = null
)

fun dummySource(index: Int = 1) = Source(
    id = index.toLong(),
    name = "source $index",
    url = "url $index",
    lastFetched = 0L,
    isActive = true
)

fun dummyFoundFeed(index: Int = 1) = FoundFeed(
    id = index.toLong(),
    url = "url $index",
    nArticles = index,
    name = "name $index"
)