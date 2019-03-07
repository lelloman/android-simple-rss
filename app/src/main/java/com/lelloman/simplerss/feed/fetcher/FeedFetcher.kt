package com.lelloman.simplerss.feed.fetcher

import com.lelloman.common.http.HttpClient
import com.lelloman.common.http.HttpClientException
import com.lelloman.common.http.request.HttpRequest
import com.lelloman.common.logger.LoggerFactory
import com.lelloman.common.view.MeteredConnectionChecker
import com.lelloman.simplerss.feed.FeedParser
import com.lelloman.simplerss.feed.ParsedFeed
import com.lelloman.simplerss.feed.exception.InvalidFeedTagException
import com.lelloman.simplerss.feed.exception.MalformedXmlException
import com.lelloman.simplerss.html.HtmlParser
import com.lelloman.simplerss.persistence.db.model.Article
import com.lelloman.simplerss.persistence.db.model.Source
import com.lelloman.simplerss.persistence.settings.AppSettings
import io.reactivex.Maybe
import io.reactivex.Single

class FeedFetcher(
    private val httpClient: HttpClient,
    private val feedParser: FeedParser,
    private val htmlParser: HtmlParser,
    private val meteredConnectionChecker: MeteredConnectionChecker,
    private val appSettings: AppSettings,
    loggerFactory: LoggerFactory
) {

    private val logger = loggerFactory.getLogger(javaClass)

    fun fetchFeed(source: Source): Maybe<Pair<Source, List<Article>>> = appSettings
        .useMeteredNetwork
        .firstOrError()
        .toMaybe()
        .onErrorComplete()
        .filter { useMeteredNetwork ->
            useMeteredNetwork || !meteredConnectionChecker.isNetworkMetered()
        }
        .flatMap { _ ->
            httpClient
                .request(HttpRequest(source.url))
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

    fun testUrl(url: String): Single<TestResult> = httpClient
        .request(HttpRequest(url))
        .flatMap { feedParser.parseFeeds(it.stringBody) }
        .map { parsedFeeds ->
            val articles = parsedFeeds.map { parsedFeedToArticle(dummySource(url), it) }
            if (articles.isEmpty()) {
                EmptySource
            } else {
                Success(articles.size, parsedFeeds.title)
            }
        }
        .onErrorResumeNext { error: Throwable ->
            val result = when (error) {
                is InvalidFeedTagException,
                is MalformedXmlException -> XmlError
                is HttpClientException -> HttpError
                else -> UnknownError
            }
            Single.just(result)
        }
        .doOnSuccess {
            logger.d("testUrl() $url result $it")
        }

    private fun dummySource(url: String) = Source(
        id = 0L,
        name = "dummy",
        url = url,
        lastFetched = 0L,
        isActive = true,
        favicon = null
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
            sourceId = source.id,
            imageUrl = imageUrl
        )
    }
}