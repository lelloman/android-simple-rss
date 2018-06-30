package com.lelloman.read.testutils

import com.lelloman.read.persistence.db.model.Article

fun dummyArticle(index: Int = 1) = Article(
    id = index.toLong(),
    title = "article $index",
    subtitle = "subtitle $index",
    content = "content $index",
    link = "link $index",
    imageUrl = "image url $index",
    time = index.toLong(),
    sourceName = "source 1",
    sourceId = index.toLong()
)