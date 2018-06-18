package com.lelloman.read.feed

import com.lelloman.read.persistence.model.Article
import com.lelloman.read.persistence.model.Source

class ParsedFeedToArticleMapper {

    fun map(parsedFeed: ParsedFeed, source: Source): Article {
        return Article(
            id = 0L,
            title = parsedFeed.title,
            subtitle = parsedFeed.subtitle,
            time = parsedFeed.timestamp,
            link = parsedFeed.link,
            content = "",
            sourceName = source.name,
            sourceId = source.id
        )
    }
}