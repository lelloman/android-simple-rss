package com.lelloman.simplerss.feed.fetcher

import com.lelloman.common.logger.LoggerFactory
import com.lelloman.common.view.MeteredConnectionChecker
import io.reactivex.Maybe
import io.reactivex.Single

class FeedFetcher(
    private val httpClient: com.lelloman.simplerss.http.HttpClient,
    private val feedParser: com.lelloman.simplerss.feed.FeedParser,
    private val htmlParser: com.lelloman.simplerss.html.HtmlParser,
    private val meteredConnectionChecker: MeteredConnectionChecker,
    private val appSettings: com.lelloman.simplerss.persistence.settings.AppSettings,
    loggerFactory: LoggerFactory
) {

    private val logger = loggerFactory.getLogger(javaClass)

    fun fetchFeed(source: com.lelloman.simplerss.persistence.db.model.Source): Maybe<Pair<com.lelloman.simplerss.persistence.db.model.Source, List<com.lelloman.simplerss.persistence.db.model.Article>>> = appSettings
        .useMeteredNetwork
        .firstOrError()
        .toMaybe()
        .onErrorComplete()
        .filter { useMeteredNetwork ->
            useMeteredNetwork || !meteredConnectionChecker.isNetworkMetered()
        }
        .flatMap { _ ->
            httpClient
                .request(com.lelloman.simplerss.http.HttpRequest(source.url))
                .filter { it.isSuccessful }
                .flatMap {
                    feedParser
                        .parseFeeds(it.stringBody)
                        .toMaybe()
                }
                .map { parsedFeeds ->
                    parsedFeeds.map {
                        parsedFeedToArticle(source, it)
                    }
                }
                .map { source to it }
        }

    fun testUrl(url: String): Single<com.lelloman.simplerss.feed.fetcher.TestResult> = httpClient
        .request(com.lelloman.simplerss.http.HttpRequest(url))
        .flatMap { feedParser.parseFeeds(it.stringBody) }
        .map { parsedFeeds ->
            val articles = parsedFeeds.map { parsedFeedToArticle(dummySource(url), it) }
            if (articles.isEmpty()) {
                com.lelloman.simplerss.feed.fetcher.EmptySource
            } else {
                com.lelloman.simplerss.feed.fetcher.Success(articles.size, parsedFeeds.title)
            }
        }
        .onErrorResumeNext { error: Throwable ->
            val result = when (error) {
                is com.lelloman.simplerss.feed.exception.InvalidFeedTagException,
                is com.lelloman.simplerss.feed.exception.MalformedXmlException -> com.lelloman.simplerss.feed.fetcher.XmlError
                is com.lelloman.simplerss.http.HttpClientException -> com.lelloman.simplerss.feed.fetcher.HttpError
                else -> com.lelloman.simplerss.feed.fetcher.UnknownError
            }
            Single.just(result)
        }
        .doOnSuccess {
            logger.d("testUrl() $url result $it")
        }

    private fun dummySource(url: String) = com.lelloman.simplerss.persistence.db.model.Source(
        id = 0L,
        name = "dummy",
        url = url,
        lastFetched = 0L,
        isActive = true,
        favicon = null
    )

    private fun parsedFeedToArticle(source: com.lelloman.simplerss.persistence.db.model.Source, parsedFeed: com.lelloman.simplerss.feed.ParsedFeed): com.lelloman.simplerss.persistence.db.model.Article {
        val (title, imagesUrl1) = htmlParser.parseTextAndImagesUrls(parsedFeed.title)
        val (subtitle, imagesUrl2) = htmlParser.parseTextAndImagesUrls(parsedFeed.subtitle)

        val imageUrl = when {
            imagesUrl1.isNotEmpty() -> imagesUrl1[0]
            imagesUrl2.isNotEmpty() -> imagesUrl2[0]
            else -> null
        }

        return com.lelloman.simplerss.persistence.db.model.Article(
            id = 0L,
            title = title,
            subtitle = subtitle,
            time = parsedFeed.timestamp,
            link = parsedFeed.link,
            content = "",
            sourceId = source.id,
            imageUrl = imageUrl
        )
    }
}