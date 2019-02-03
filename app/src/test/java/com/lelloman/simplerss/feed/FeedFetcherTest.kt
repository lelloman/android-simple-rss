package com.lelloman.simplerss.feed

import com.google.common.truth.Truth.assertThat
import com.lelloman.simplerss.testutils.MockLoggerFactory
import com.lelloman.simplerss.testutils.MockMeteredConnectionChecker
import com.lelloman.simplerss.testutils.dummySource
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argThat
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Test

class FeedFetcherTest {
    private val httpClient: com.lelloman.simplerss.http.HttpClient = mock()
    private val feedParser: com.lelloman.simplerss.feed.FeedParser = mock()
    private val htmlParser: com.lelloman.simplerss.html.HtmlParser = mock()
    private val meteredConnectionChecker = MockMeteredConnectionChecker()
    private val appSettings = com.lelloman.simplerss.mock.MockAppSettings()
    private val loggerFactory = MockLoggerFactory()

    private val tested = com.lelloman.simplerss.feed.fetcher.FeedFetcher(
        httpClient = httpClient,
        feedParser = feedParser,
        htmlParser = htmlParser,
        meteredConnectionChecker = meteredConnectionChecker,
        appSettings = appSettings,
        loggerFactory = loggerFactory
    )

    @Test
    fun `makes http call when fetching feed`() {
        givenCanUseMeteredNetwork()
        givenHttpSuccessfulResponse()

        tested.fetchFeed(com.lelloman.simplerss.feed.FeedFetcherTest.Companion.SOURCE).test()

        verify(httpClient).request(argThat { url == com.lelloman.simplerss.feed.FeedFetcherTest.Companion.SOURCE.url })
    }

    @Test
    fun `parses body of successful response`() {
        givenCanUseMeteredNetwork()
        givenHttpSuccessfulResponse()

        tested.fetchFeed(com.lelloman.simplerss.feed.FeedFetcherTest.Companion.SOURCE).test()

        verify(feedParser).parseFeeds(com.lelloman.simplerss.feed.FeedFetcherTest.Companion.SUCCESSFUL_RESPONSE.stringBody)
    }

    @Test
    fun `does not parse body of unsuccessful response`() {
        givenCanUseMeteredNetwork()
        givenHttpUnsuccessfulResponse()

        val tester = tested.fetchFeed(com.lelloman.simplerss.feed.FeedFetcherTest.Companion.SOURCE).test()

        verifyZeroInteractions(feedParser)
        tester.assertValueCount(0)
        tester.assertComplete()
    }

    @Test
    fun `returns source and articles`() {
        givenCanUseMeteredNetwork()
        givenHttpSuccessfulResponse()
        givenParsesFeed()
        givenParsesHtml()

        val tester = tested.fetchFeed(com.lelloman.simplerss.feed.FeedFetcherTest.Companion.SOURCE).test()

        tester.assertNoErrors()
        val (source, articles) = tester.values()[0]
        assertThat(source).isEqualTo(com.lelloman.simplerss.feed.FeedFetcherTest.Companion.SOURCE)
        assertThat(articles).hasSize(com.lelloman.simplerss.feed.FeedFetcherTest.Companion.PARSED_FEED.size)
        articles.forEachIndexed { index, article ->
            com.lelloman.simplerss.feed.FeedFetcherTest.Companion.PARSED_FEED[index].let { feed ->
                assertThat(article.title).isEqualTo(feed.title)
                assertThat(article.subtitle).isEqualTo((feed.subtitle))
                assertThat(article.time).isEqualTo(feed.timestamp)
                assertThat(article.link).isEqualTo(feed.link)
            }
        }
    }

    @Test
    fun `does not fetch feed if cant use metered network and network is metered`() {
        givenCannotUseMeteredNetwork()
        givenMeteredNetwork()

        val tester = tested.fetchFeed(dummySource()).test()

        tester.assertComplete()
        tester.assertNoValues()
    }

    @Test
    fun `fetches feed if cant user metered network and network is non un-metered`() {
        givenCannotUseMeteredNetwork()
        givenUnMeteredNetwork()
        givenHttpSuccessfulResponse()
        givenParsesHtml()
        givenParsesFeed()

        val tester = tested.fetchFeed(com.lelloman.simplerss.feed.FeedFetcherTest.Companion.SOURCE).test()

        tester.assertComplete()
        tester.assertValueCount(1)
        tester.assertValueAt(0) {
            it.first == com.lelloman.simplerss.feed.FeedFetcherTest.Companion.SOURCE && it.second.size == 2
        }
    }

    @Test
    fun `returns successful url test result`() {
        givenHttpSuccessfulResponse()
        givenParsesHtml()
        givenParsesFeed()

        val tester = tested.testUrl("asd").test()

        tester.assertComplete()
        tester.assertValueCount(1)
        tester.assertValueAt(0) { it is com.lelloman.simplerss.feed.fetcher.Success && it.nArticles == com.lelloman.simplerss.feed.FeedFetcherTest.Companion.PARSED_FEED.size }
    }

    @Test
    fun `returns empty source url test result`() {
        givenHttpSuccessfulResponse()
        givenParsesFeed(com.lelloman.simplerss.feed.ParsedFeeds())

        val tester = tested.testUrl("asd").test()

        tester.assertComplete()
        tester.assertValues(com.lelloman.simplerss.feed.fetcher.EmptySource)
    }

    @Test
    fun `returns xml error test result for invalid feed tag exception`() {
        givenHttpSuccessfulResponse()
        whenever(feedParser.parseFeeds(any())).thenReturn(Single.error(com.lelloman.simplerss.feed.exception.InvalidFeedTagException("")))

        val tester = tested.testUrl("asd").test()

        tester.assertComplete()
        tester.assertValues(com.lelloman.simplerss.feed.fetcher.XmlError)
    }


    @Test
    fun `returns xml error test result for malformed xml exception`() {
        givenHttpSuccessfulResponse()
        whenever(feedParser.parseFeeds(any())).thenReturn(Single.error(com.lelloman.simplerss.feed.exception.MalformedXmlException(Exception())))

        val tester = tested.testUrl("asd").test()

        tester.assertComplete()
        tester.assertValues(com.lelloman.simplerss.feed.fetcher.XmlError)
    }

    @Test
    fun `returns http error test result for http exception`() {
        whenever(httpClient.request(any())).thenReturn(Single.error(com.lelloman.simplerss.http.HttpClientException(Exception())))

        val tester = tested.testUrl("asd").test()

        tester.assertComplete()
        tester.assertValues(com.lelloman.simplerss.feed.fetcher.HttpError)
    }

    @Test
    fun `returns unknown error test result if feed parser throws unexpected exception`() {
        givenHttpSuccessfulResponse()
        whenever(feedParser.parseFeeds(any())).thenReturn(Single.error(IllegalAccessException("wut")))

        val tester = tested.testUrl("asd").test()

        tester.assertComplete()
        tester.assertValues(com.lelloman.simplerss.feed.fetcher.UnknownError)
    }

    @Test
    fun `returns unknown error test result if html parser throws unexpected exception`() {
        givenHttpSuccessfulResponse()
        whenever(htmlParser.parseTextAndImagesUrls(any())).then { throw IllegalAccessException("wut") }

        val tester = tested.testUrl("asd").test()

        tester.assertComplete()
        tester.assertValues(com.lelloman.simplerss.feed.fetcher.UnknownError)
    }

    private fun givenUnMeteredNetwork() {
        meteredConnectionChecker.isNetworkMeteredValue = false
    }

    private fun givenMeteredNetwork() {
        meteredConnectionChecker.isNetworkMeteredValue = true
    }

    private fun givenCannotUseMeteredNetwork() {
        appSettings.providedUseMeteredNetwork = Observable.just(false)
    }

    private fun givenCanUseMeteredNetwork() {
        appSettings.providedUseMeteredNetwork = Observable.just(true)
    }

    private fun givenHttpSuccessfulResponse() {
        whenever(httpClient.request(any())).thenReturn(Single.just(com.lelloman.simplerss.feed.FeedFetcherTest.Companion.SUCCESSFUL_RESPONSE))
    }

    private fun givenHttpUnsuccessfulResponse() {
        whenever(httpClient.request(any()))
            .thenReturn(Single.just(com.lelloman.simplerss.http.HttpResponse(500, false, ByteArray(0))))
    }

    private fun givenParsesFeed(feeds: com.lelloman.simplerss.feed.ParsedFeeds = com.lelloman.simplerss.feed.FeedFetcherTest.Companion.PARSED_FEED) {
        whenever(feedParser.parseFeeds(any())).thenReturn(Single.just(feeds))
    }

    private fun givenParsesHtml() {
        whenever(htmlParser.parseTextAndImagesUrls(any())).thenAnswer {
            com.lelloman.simplerss.html.HtmlParser.TextAndImagesUrls(
                it.arguments[0] as String,
                emptyList()
            )
        }
    }

    private companion object {
        val SOURCE = com.lelloman.simplerss.persistence.db.model.Source(
            id = 1L,
            name = "the source",
            url = "http://www.staceppa.com",
            lastFetched = 0L,
            isActive = true
        )

        val SUCCESSFUL_RESPONSE = com.lelloman.simplerss.http.HttpResponse(200, true, "the body".toByteArray())

        val PARSED_FEED = com.lelloman.simplerss.feed.ParsedFeeds(mutableListOf(
            com.lelloman.simplerss.feed.ParsedFeed(
                title = "title 1",
                subtitle = "subtitle 1",
                link = "link 1",
                timestamp = 1L
            ),
            com.lelloman.simplerss.feed.ParsedFeed(
                title = "title 2",
                subtitle = "subtitle 2",
                link = "link 2",
                timestamp = 2L
            )
        ))
    }
}