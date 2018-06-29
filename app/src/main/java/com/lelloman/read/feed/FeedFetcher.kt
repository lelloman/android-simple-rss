package com.lelloman.read.feed

import com.lelloman.read.http.HttpClient
import com.lelloman.read.http.HttpClientException
import com.lelloman.read.http.HttpRequest
import com.lelloman.read.persistence.db.model.Article
import com.lelloman.read.persistence.db.model.Source
import com.lelloman.read.utils.HtmlParser
import io.reactivex.Maybe
import io.reactivex.Single

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

    fun testUrl(url: String): Single<TestResult> = httpClient
        .request(HttpRequest(url))
        .flatMap { feedParser.parseFeeds(it.body) }
        .map {
            val articles = it.map { parsedFeedToArticle(dummySource(url), it) }
            if (articles.isEmpty()) {
                TestResult.EMPTY_SOURCE
            } else {
                TestResult.SUCCESS
            }
        }
        .onErrorResumeNext { error: Throwable ->
            val result = when (error) {
                is InvalidFeedTagException,
                is MalformedXmlException -> TestResult.XML_ERROR
                is HttpClientException -> TestResult.HTTP_ERROR
                else -> TestResult.UNKNOWN_ERROR
            }
            Single.just(result)
        }

    private fun dummySource(url: String) = Source(
        id = 0L,
        name = "dummy",
        url = url,
        lastFetched = 0L,
        isActive = true
    )

    private fun parsedFeedToArticle(source: Source, parsedFeed: ParsedFeed): Article {
        val (title, imagesUrl1) = htmlParser.parseTextAndImagesUrls(parsedFeed.title)
        val (subtitle, imagesUrl2) = htmlParser.parseTextAndImagesUrls(parsedFeed.subtitle)

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

    enum class TestResult {
        HTTP_ERROR,
        XML_ERROR,
        EMPTY_SOURCE,
        UNKNOWN_ERROR,
        SUCCESS
    }
}