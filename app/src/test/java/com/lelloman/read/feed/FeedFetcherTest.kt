package com.lelloman.read.feed

import com.google.common.truth.Truth.assertThat
import com.lelloman.read.http.HttpClient
import com.lelloman.read.http.HttpResponse
import com.lelloman.read.persistence.db.model.Source
import com.lelloman.read.utils.HtmlParser
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argThat
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.junit.Test

class FeedFetcherTest {
    private val httpClient: HttpClient = mock()
    private val feedParser: FeedParser = mock()
    private val htmlParser: HtmlParser = mock()

    private val tested = FeedFetcher(
        httpClient = httpClient,
        feedParser = feedParser,
        htmlParser = htmlParser
    )

    @Test
    fun `makes http call when fetching feed`() {
        givenHttpSuccessfulResponse()

        tested.fetchFeed(SOURCE).test()

        verify(httpClient).request(argThat { url == SOURCE.url })
    }

    @Test
    fun `parses body of successful response`() {
        givenHttpSuccessfulResponse()

        tested.fetchFeed(SOURCE).test()

        verify(feedParser).parseFeeds(SUCCESSFUL_RESPONSE.body)
    }

    @Test
    fun `does not parse body of unsuccessful response`() {
        givenHttpUnsuccessfulResponse()

        val tester = tested.fetchFeed(SOURCE).test()

        verifyZeroInteractions(feedParser)
        tester.assertValueCount(0)
        tester.assertComplete()
    }

    @Test
    fun `returns source and articles`() {
        givenHttpSuccessfulResponse()
        givenParsesFeed()
        givenParsesHtml()

        val tester = tested.fetchFeed(SOURCE).test()

        tester.assertNoErrors()
        val (source, articles) = tester.values()[0]
        assertThat(source).isEqualTo(SOURCE)
        assertThat(articles).hasSize(PARSED_FEED.size)
        articles.forEachIndexed { index, article ->
            PARSED_FEED[index].let { feed ->
                assertThat(article.title).isEqualTo(feed.title)
                assertThat(article.subtitle).isEqualTo((feed.subtitle))
                assertThat(article.time).isEqualTo(feed.timestamp)
                assertThat(article.link).isEqualTo(feed.link)
            }
        }
    }

    private fun givenHttpSuccessfulResponse() {
        whenever(httpClient.request(any())).thenReturn(Single.just(SUCCESSFUL_RESPONSE))
    }

    private fun givenHttpUnsuccessfulResponse() {
        whenever(httpClient.request(any()))
            .thenReturn(Single.just(HttpResponse(500, false, "")))
    }

    private fun givenParsesFeed() {
        whenever(feedParser.parseFeeds(any())).thenReturn(Single.just(PARSED_FEED))
    }

    private fun givenParsesHtml() {
        whenever(htmlParser.parseTextAndImagesUrls(any())).thenAnswer {
            it.arguments[0] as String to emptyList<String>()
        }
    }

    private companion object {
        val SOURCE = Source(
            id = 1L,
            name = "the source",
            url = "http://www.staceppa.com",
            lastFetched = 0L,
            isActive = true
        )

        val SUCCESSFUL_RESPONSE = HttpResponse(200, true, "the body")

        val PARSED_FEED = listOf(
            ParsedFeed(
                title = "title 1",
                subtitle = "subtitle 1",
                link = "link 1",
                timestamp = 1L
            ),
            ParsedFeed(
                title = "title 2",
                subtitle = "subtitle 2",
                link = "link 2",
                timestamp = 2L
            )
        )
    }
}