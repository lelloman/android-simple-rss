package com.lelloman.simplerss.feed.finder

import com.google.common.truth.Truth.assertThat
import com.lelloman.common.jvmtestutils.MockLoggerFactory
import com.lelloman.common.jvmtestutils.MockUrlValidator
import com.lelloman.simplerss.html.Doc
import com.lelloman.simplerss.html.HtmlParser
import com.lelloman.simplerss.html.element.ADocElement
import com.lelloman.simplerss.html.element.LinkDocElement
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Test

class FeedFinderParserTest {

    private val urlValidator = MockUrlValidator {
        foundBaseUrlWithProtocol = BASE_URL
    }

    private val htmlParser: HtmlParser = mock()

    private val tested = FeedFinderParser(
        urlValidator = urlValidator,
        htmlParser = htmlParser,
        loggerFactory = MockLoggerFactory()
    )

    @Test
    fun `parses doc and assigns base url`() {
        val html = "html stuff"
        val doc = Doc()
        whenever(htmlParser.parseDoc(any(), any(), any())).thenReturn(doc)

        val tester = tested.parseDoc(URL_WITH_PATH, html).test()

        verify(htmlParser).parseDoc(
            url = URL_WITH_PATH,
            baseUrl = BASE_URL,
            html = html
        )
        tester.assertValues(doc)
    }

    @Test
    fun `drops error from url validator when parsing doc`() {
        urlValidator.findBaseUrlWithProtocolThrowsException = true

        val tester = tested.parseDoc(URL_WITH_PATH, "").test()

        tester.assertNoErrors()
        tester.assertValueCount(0)
        tester.assertComplete()
    }

    @Test
    fun `drops error from html parser when parsing doc`() {
        whenever(htmlParser.parseDoc(any(), any(), any())).thenThrow(RuntimeException())

        val tester = tested.parseDoc(URL_WITH_PATH, "").test()

        tester.assertNoErrors()
        tester.assertValueCount(0)
        tester.assertComplete()
    }

    @Test
    fun `returns url guesses when finding candidate urls and doc has a url`() {
        val tester = tested.findCandidateUrls(DOC_WITH_URL).test()

        tester.assertValueSet(setOf(
            "$URL_WITH_PATH/feed",
            "$URL_WITH_PATH/feed.xml",
            "$URL_WITH_PATH/rss",
            "$URL_WITH_PATH/rss.xml"
        ))
    }

    @Test
    fun `does not return url slash feed when finding candidate urls and doc does not have a url`() {
        val tester = tested.findCandidateUrls(DOC_WITHOUT_URL).test()

        tester.assertValueCount(0)
        tester.assertComplete()
    }

    @Test
    fun `iterates all doc elements when finding candidate urls`() {
        val doc: Doc = mock()

        tested.findCandidateUrls(doc).test()

        verify(doc).iterate(any())
    }

    @Test
    fun `returns all a tag elements with rss or feed in the text when finding candidate urls`() {
        val validLink1 = "magariquestoeunfeed.com"
        val validLink2 = "www.quicisonorss.com"
        val docElements = listOf(
            ADocElement(parent = mock(), href = validLink1),
            ADocElement(parent = mock(), href = validLink2),
            ADocElement(parent = mock(), href = "questopropriono")
        )
        val doc = Doc(url = null, baseUrl = null).apply {
            children = docElements
        }

        val tester = tested.findCandidateUrls(doc).test()

        tester.assertValueCount(2)
        tester.assertComplete()
        tester.assertValueSet(mutableListOf(validLink1, validLink2))
    }

    @Test
    fun `returns links with specific type when finding candidate urls`() {
        val validLink1 = "magariquestoeunfeed.com"
        val validLink2 = "www.quicisonorss.com"
        val validLink3 = "yeee me tooo"
        val docElements = listOf(
            LinkDocElement(parent = mock(), href = validLink1, linkType = "application/rss+xml"),
            LinkDocElement(parent = mock(), href = validLink2, linkType = "text/xml"),
            LinkDocElement(parent = mock(), href = validLink3, linkType = "application/atom+xml"),
            LinkDocElement(parent = mock(), href = "questopropriono", linkType = "nope")
        )
        val doc = Doc(url = null, baseUrl = null).apply {
            children = docElements
        }

        val tester = tested.findCandidateUrls(doc).test()

        tester.assertValueCount(3)
        tester.assertComplete()
        tester.assertValueSet(mutableListOf(validLink1, validLink2, validLink3))
    }

    @Test
    fun `prepends base url to links if necessary when finding candidate urls`() {
        val path = "/rssasdasd"
        val noPath = "feedthis"
        val pathWithBaseUrl = "http://www.staceppa.com/asd"
        urlValidator.wheneverMaybePrependBaseUrl(BASE_URL, path) { pathWithBaseUrl }
        val doc = givenDocWithCandidatesAndUrl(listOf(path, noPath))

        val tester = tested.findCandidateUrls(doc).test()

        assertThat(tester.values()).contains(noPath)
        assertThat(tester.values()).contains(pathWithBaseUrl)
        tester.assertValueCount(6) // +4 for url guesses
        tester.assertComplete()
    }

    @Test
    fun `removes duplicates when finding candidate urls`() {
        val url = "rssurl"
        val doc = givenDocWithCandidatesAndNoUrl(listOf(url, url, url))

        val tester = tested.findCandidateUrls(doc).test()

        tester.assertValueCount(1)
        tester.assertComplete()
    }

    private fun givenDocWithCandidatesAndUrl(candidateUrls: List<String>) =
        Doc(baseUrl = BASE_URL, url = URL_WITH_PATH).apply {
            children = candidateUrls.map { ADocElement(parent = mock(), href = it) }
        }

    private fun givenDocWithCandidatesAndNoUrl(candidateUrls: List<String>) =
        Doc(null, null).apply {
            children = candidateUrls.map { ADocElement(parent = mock(), href = it) }
        }

    private companion object {
        const val BASE_URL = "http://www.staceppa.it"
        const val URL_WITH_PATH = "$BASE_URL/asd/asd"
        val DOC_WITH_URL = Doc(
            baseUrl = BASE_URL,
            url = URL_WITH_PATH
        )
        val DOC_WITHOUT_URL = Doc(null, null)
    }
}