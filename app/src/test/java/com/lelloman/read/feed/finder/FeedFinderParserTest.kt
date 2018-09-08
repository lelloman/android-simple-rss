package com.lelloman.read.feed.finder

import com.lelloman.read.html.Doc
import com.lelloman.read.html.HtmlParser
import com.lelloman.read.html.element.ADocElement
import com.lelloman.read.html.element.LinkDocElement
import com.lelloman.read.mock.MockLoggerFactory
import com.lelloman.read.mock.MockUrlValidator
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
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
    fun `returns url slash feed when finding candidate urls and doc has an url`() {
        val tester = tested.findCandidateUrls(DOC_WITH_URL).test()

        tester.assertValues("$URL_WITH_PATH/feed")
    }

    @Test
    fun `does not return url slash feed when finding candidate urls and doc does not have an url`() {
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

        tester.assertValueCount(3)
        tester.assertValueSet(mutableListOf("$URL_WITH_PATH/feed", pathWithBaseUrl, noPath))
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