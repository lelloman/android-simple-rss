package com.lelloman.read.feed.finder

import android.net.ConnectivityManager
import com.google.common.truth.Truth.assertThat
import com.lelloman.read.core.MeteredConnectionChecker
import com.lelloman.read.core.TimeProvider
import com.lelloman.read.core.logger.Logger
import com.lelloman.read.core.logger.LoggerFactory
import com.lelloman.read.feed.FeedParser
import com.lelloman.read.feed.fetcher.FeedFetcher
import com.lelloman.read.html.HtmlParser
import com.lelloman.read.http.HttpClient
import com.lelloman.read.http.HttpRequest
import com.lelloman.read.http.HttpResponse
import com.lelloman.read.persistence.settings.AppSettings
import com.lelloman.read.utils.UrlValidator
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import io.reactivex.Maybe
import io.reactivex.Single
import org.junit.Test

class FeedFinderIntegrationTest {

    private val httpClient = object : HttpClient {

        private val urlMap = mutableMapOf<String, HttpResponse>()

        fun map(url: String, stringBody: String) = map(
            url = url,
            httpResponse = HttpResponse(200, true, stringBody.toByteArray())
        )

        fun map(url: String, httpResponse: HttpResponse) {
            urlMap[url] = httpResponse
        }

        override fun request(request: HttpRequest): Single<HttpResponse> = Maybe
            .fromCallable {
                urlMap[request.url]
                    ?: HttpResponse(code = 404, isSuccessful = false, body = byteArrayOf())
            }
            .toSingle()
    }

    private val urlValidator = UrlValidator()
    private val htmlParser = HtmlParser()

    private val loggerFactory = object : LoggerFactory {
        override fun getLogger(tag: String): Logger = mock()
    }

    private val connectivityManager: ConnectivityManager = mock {
        on { isActiveNetworkMetered }.thenReturn(false)
    }
    private val meteredConnectionChecker = MeteredConnectionChecker(mock {
        on { getSystemService(any()) }.thenReturn(connectivityManager)
    })

    private val appSettings: AppSettings = mock {

    }

    private val timeProvider = TimeProvider()

    private val feedParser = FeedParser(timeProvider = timeProvider)

    private val feedFinderParser = FeedFinderParser(
        urlValidator = urlValidator,
        htmlParser = htmlParser,
        loggerFactory = loggerFactory
    )

    private val feedFetcher = FeedFetcher(
        httpClient = httpClient,
        feedParser = feedParser,
        htmlParser = htmlParser,
        meteredConnectionChecker = meteredConnectionChecker,
        appSettings = appSettings,
        loggerFactory = loggerFactory
    )

    private val feedFinderHttpClient = FeedFinderHttpClient(
        httpClient = httpClient,
        urlValidator = urlValidator
    )

    private val tested = FeedFinder(
        httpClient = feedFinderHttpClient,
        parser = feedFinderParser,
        feedFetcher = feedFetcher,
        loggerFactory = loggerFactory
    )

    @Test
    fun `finds links in html 1`() {
        httpClient.map(URL_1, HTML_1)
        httpClient.map("$URL_1/feed", VALID_FEED_XML)
        httpClient.map("$URL_1/somefeed", VALID_FEED_XML)
        httpClient.map("http://www.staceppa.com", VALID_FEED_XML)

        val tester = tested.findValidFeedUrls(URL_1).test()

        tester.assertComplete()
        tester.assertValueCount(3)
        tester.values().apply {
            assertThat(this).contains(FoundFeed(1, "$URL_1/feed", 1, "RSS di   - ANSA.it"))
            assertThat(this).contains(FoundFeed(2, "http://www.staceppa.com", 1, "RSS di   - ANSA.it"))
            assertThat(this).contains(FoundFeed(3, "$URL_1/somefeed", 1, "RSS di   - ANSA.it"))
        }
    }

    @Test
    fun `finds links in html 2`() {
        httpClient.map(URL_1, HTML_1)
        httpClient.map("$URL_1/feed", VALID_FEED_XML)
        httpClient.map("$URL_1/somefeed", HTML_2)
        httpClient.map("http://www.rsssomething.com", VALID_FEED_XML)
        httpClient.map("http://www.staceppa2.com", VALID_FEED_XML)

        val tester = tested.findValidFeedUrls(URL_1).test()

        tester.assertComplete()
        tester.assertValueCount(3)
        tester.values().apply {
            assertThat(this).contains(FoundFeed(1, "$URL_1/feed", 1, "RSS di   - ANSA.it"))
            assertThat(this).contains(FoundFeed(2, "http://www.staceppa2.com", 1, "RSS di   - ANSA.it"))
            assertThat(this).contains(FoundFeed(3, "http://www.rsssomething.com", 1, "RSS di   - ANSA.it"))
        }
    }

    private companion object {
        const val URL_1 = "http://www.url1.it"
        const val HTML_1 = """
<html>
    <head>
        <link type="application/rss+xml" href="http://www.staceppa.com" />
        <link type="text/xml" href="http://wwww.staceppa.com/2" />
    </head>
    <body>
        <p>
            asdasd
            <a href="/somefeed">asdasd</a>
            asdasd
        </p>
    </body>
</html>
        """

        const val HTML_2 = """
<html>
    <head>
        <link type="application/atom+xml" href="http://www.staceppa2.com" />
        <link type="text/xml" href="http://wwww.staceppa.com/3" />
    </head>
    <body>
        <p>
            asdasd
            <a href="http://www.rsssomething.com">asdasd</a>
            asdasd
        </p>
    </body>
</html>
        """

        const val VALID_FEED_XML = """
<rss version="2.0" xmlns:atom="http://www.w3.org/2005/Atom">
	<channel>
		<atom:link rel="self" type="application/rss+xml" href="http://www.ansa.it/sito/ansait_rss.xml"></atom:link>
		<title>   RSS di   - ANSA.it      </title>
		  <link>http://www.ansa.it/</link>
		  <description>Updated every day - FOR PERSONAL USE ONLY</description>
		<language>it</language>
		<copyright>Copyright: (C) ANSA, http://www.ansa.it/sito/static/disclaimer.html</copyright>
		<item>
		<title><![CDATA[Spia russa in ambasciata Usa a Mosca]]></title>
		<description><![CDATA[Una donna che lavorato dieci anni per il Secret Service]]></description><link>http://www.ansa.it/sito/notizie/mondo/nordamerica/2018/08/03/spia-russa-in-ambasciata-usa-a-mosca_c4381ef5-9fbd-4125-a571-ff66dd7a561d.html</link><pubDate>Fri, 3 Aug 2018 07:20:31 +0200</pubDate>
		<guid>http://www.ansa.it/sito/notizie/mondo/nordamerica/2018/08/03/spia-russa-in-ambasciata-usa-a-mosca_c4381ef5-9fbd-4125-a571-ff66dd7a561d.html</guid>
		</item>
	</channel>
</rss>
        """
    }
}