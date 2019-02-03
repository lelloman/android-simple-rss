package com.lelloman.simplerss.feed

import com.google.common.truth.Truth.assertThat
import com.lelloman.simplerss.feed.exception.InvalidFeedTagException
import com.lelloman.simplerss.feed.exception.MalformedXmlException
import com.lelloman.simplerss.feed.fetcher.EmptySource
import com.lelloman.simplerss.feed.fetcher.FeedFetcher
import com.lelloman.simplerss.feed.fetcher.HttpError
import com.lelloman.simplerss.feed.fetcher.Success
import com.lelloman.simplerss.feed.fetcher.UnknownError
import com.lelloman.simplerss.feed.fetcher.XmlError
import com.lelloman.simplerss.html.HtmlParser
import com.lelloman.simplerss.http.HttpClient
import com.lelloman.simplerss.http.HttpClientException
import com.lelloman.simplerss.http.HttpResponse
import com.lelloman.simplerss.mock.MockAppSettings
import com.lelloman.simplerss.persistence.db.model.Source
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
    private val httpClient: HttpClient = mock()
    private val feedParser: FeedParser = mock()
    private val htmlParser: HtmlParser = mock()
    private val meteredConnectionChecker = MockMeteredConnectionChecker()
    private val appSettings = MockAppSettings()
    private val loggerFactory = MockLoggerFactory()

    private val tested = FeedFetcher(
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

        tested.fetchFeed(SOURCE).test()

        verify(httpClient).request(argThat { url == url })
    }

    @Test
    fun `parses body of successful response`() {
        givenCanUseMeteredNetwork()
        givenHttpSuccessfulResponse()

        tested.fetchFeed(SOURCE).test()

        verify(feedParser).parseFeeds(SUCCESSFUL_RESPONSE.stringBody)
    }

    @Test
    fun `does not parse body of unsuccessful response`() {
        givenCanUseMeteredNetwork()
        givenHttpUnsuccessfulResponse()

        val tester = tested.fetchFeed(SOURCE).test()

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

        val tester = tested.fetchFeed(SOURCE).test()

        tester.assertComplete()
        tester.assertValueCount(1)
        tester.assertValueAt(0) {
            it.first == SOURCE && it.second.size == 2
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
        tester.assertValueAt(0) { it is Success && it.nArticles == PARSED_FEED.size }
    }

    @Test
    fun `returns empty source url test result`() {
        givenHttpSuccessfulResponse()
        givenParsesFeed(ParsedFeeds())

        val tester = tested.testUrl("asd").test()

        tester.assertComplete()
        tester.assertValues(EmptySource)
    }

    @Test
    fun `returns xml error test result for invalid feed tag exception`() {
        givenHttpSuccessfulResponse()
        whenever(feedParser.parseFeeds(any())).thenReturn(Single.error(InvalidFeedTagException("")))

        val tester = tested.testUrl("asd").test()

        tester.assertComplete()
        tester.assertValues(XmlError)
    }


    @Test
    fun `returns xml error test result for malformed xml exception`() {
        givenHttpSuccessfulResponse()
        whenever(feedParser.parseFeeds(any())).thenReturn(Single.error(MalformedXmlException(Exception())))

        val tester = tested.testUrl("asd").test()

        tester.assertComplete()
        tester.assertValues(XmlError)
    }

    @Test
    fun `returns http error test result for http exception`() {
        whenever(httpClient.request(any())).thenReturn(Single.error(HttpClientException(Exception())))

        val tester = tested.testUrl("asd").test()

        tester.assertComplete()
        tester.assertValues(HttpError)
    }

    @Test
    fun `returns unknown error test result if feed parser throws unexpected exception`() {
        givenHttpSuccessfulResponse()
        whenever(feedParser.parseFeeds(any())).thenReturn(Single.error(IllegalAccessException("wut")))

        val tester = tested.testUrl("asd").test()

        tester.assertComplete()
        tester.assertValues(UnknownError)
    }

    @Test
    fun `returns unknown error test result if html parser throws unexpected exception`() {
        givenHttpSuccessfulResponse()
        whenever(htmlParser.parseTextAndImagesUrls(any())).then { throw IllegalAccessException("wut") }

        val tester = tested.testUrl("asd").test()

        tester.assertComplete()
        tester.assertValues(UnknownError)
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
        whenever(httpClient.request(any())).thenReturn(Single.just(SUCCESSFUL_RESPONSE))
    }

    private fun givenHttpUnsuccessfulResponse() {
        whenever(httpClient.request(any()))
            .thenReturn(Single.just(HttpResponse(500, false, ByteArray(0))))
    }

    private fun givenParsesFeed(feeds: ParsedFeeds = PARSED_FEED) {
        whenever(feedParser.parseFeeds(any())).thenReturn(Single.just(feeds))
    }

    private fun givenParsesHtml() {
        whenever(htmlParser.parseTextAndImagesUrls(any())).thenAnswer {
            HtmlParser.TextAndImagesUrls(
                it.arguments[0] as String,
                emptyList()
            )
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

        val SUCCESSFUL_RESPONSE = HttpResponse(200, true, "the body".toByteArray())

        val PARSED_FEED = ParsedFeeds(mutableListOf(
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
        ))
    }
}