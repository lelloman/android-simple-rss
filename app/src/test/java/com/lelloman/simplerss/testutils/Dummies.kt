package com.lelloman.simplerss.testutils

fun dummyArticle(index: Int = 1) = com.lelloman.simplerss.persistence.db.model.Article(
    id = index.toLong(),
    title = "article $index",
    subtitle = "subtitle $index",
    content = "content $index",
    link = "link $index",
    imageUrl = "image url $index",
    time = index.toLong(),
    sourceId = index.toLong()
)

fun dummySourceArticle(index: Int = 1) = com.lelloman.simplerss.persistence.db.model.SourceArticle(
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

fun dummySource(index: Int = 1) = com.lelloman.simplerss.persistence.db.model.Source(
    id = index.toLong(),
    name = "source $index",
    url = "url $index",
    lastFetched = 0L,
    isActive = true
)

fun dummyFoundFeed(index: Int = 1) = com.lelloman.simplerss.feed.finder.FoundFeed(
    id = index.toLong(),
    url = "url $index",
    nArticles = index,
    name = "name $index"
)