package com.lelloman.read.feed

import com.lelloman.read.http.HttpClient
import com.lelloman.read.http.HttpRequest
import com.lelloman.read.persistence.db.model.Article
import com.lelloman.read.persistence.db.model.Source
import com.lelloman.read.utils.HtmlParser
import io.reactivex.Maybe

class FeedFetcher(
    private val httpClient: HttpClient,
    private val feedParser: FeedParser,
    private val htmlParser: HtmlParser
) {

    fun fetchFeed(source: Source): Maybe<Pair<Source, List<Article>>> = httpClient
        .request(HttpRequest(source.url))
        .filter { it.isSuccessful }
        .flatMap { feedParser.parseFeeds(it.body).toMaybe() }
        .map {
            it.map {
                parsedFeedToArticle(source, it)
            }
        }
        .map { source to it }

    private fun parsedFeedToArticle(source: Source, parsedFeed: ParsedFeed): Article {
        val (title, imagesUrl1) = htmlParser.withHtml(parsedFeed.title)
        val (subtitle, imagesUrl2) = htmlParser.withHtml(parsedFeed.subtitle)

        val imageUrl = when {
            imagesUrl1.isNotEmpty() -> imagesUrl1[0]
            imagesUrl2.isNotEmpty() -> imagesUrl2[0]
            else -> null
        }

        return Article(
            id = 0L,
            title = title,
            subtitle = subtitle,
            time = parsedFeed.timestamp,
            link = parsedFeed.link,
            content = "",
            sourceName = source.name,
            sourceId = source.id,
            imageUrl = imageUrl
        )
    }
}