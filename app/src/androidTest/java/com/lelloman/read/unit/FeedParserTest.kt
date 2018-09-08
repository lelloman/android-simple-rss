package com.lelloman.read.unit

import com.google.common.truth.Truth
import com.lelloman.read.core.TimeProvider
import com.lelloman.read.feed.FeedParser
import com.lelloman.read.feed.exception.InvalidFeedTagException
import com.lelloman.read.feed.exception.MalformedXmlException
import com.lelloman.read.testutils.Xmls
import io.reactivex.Observable
import org.junit.Test


class FeedParserTest {

    private val time = 0L
    private val timeProvider = object : TimeProvider() {
        override fun nowUtcMs() = time
    }

    private val tested = FeedParser(timeProvider)

    @Test
    fun throwsMalformedXmlException() {
        val tester = Xmls.readFile(Xmls.MALFORMED_XML1)
            .flatMap(tested::parseFeeds)
            .test()

        tester.assertError { it is MalformedXmlException }
    }

    @Test
    fun throwsInvalidRootTagException() {
        val tester = Xmls.readFile(Xmls.INVALID_ROOT_TAG_XML)
            .flatMap(tested::parseFeeds)
            .test()

        tester.assertError { it is InvalidFeedTagException }
    }

    @Test
    fun parsesCorrectNumberOfFeedsFromXmls() {
        val test = Observable.fromIterable(Xmls.ALL_RSS)
            .flatMapSingle(Xmls::readFile)
            .flatMapSingle(tested::parseFeeds)
            .test()

        test.assertValueCount(6)
        test.assertValueAt(0) { it.size == 23 }
        test.assertValueAt(1) { it.size == 118 }
        test.assertValueAt(2) { it.size == 118 }
        test.assertValueAt(3) { it.size == 3 }
        test.assertValueAt(4) { it.size == 10 }
        test.assertValueAt(5) { it.size == 30 }
    }

    @Test
    fun parsesFeedsFromSampleXml() {
        val tester = Xmls.readFile(Xmls.SAMPLE)
            .flatMap(tested::parseFeeds)
            .test()

        tester.assertNoErrors()
        tester.assertValueCount(1)
        val feeds = tester.values()[0]
        Truth.assertThat(feeds[0]).isEqualTo(Xmls.SAMPLE_FEEDS[0])
        Truth.assertThat(feeds[1]).isEqualTo(Xmls.SAMPLE_FEEDS[1])
        Truth.assertThat(feeds[2]).isEqualTo(Xmls.SAMPLE_FEEDS[2])
    }

    @Test
    fun parsesFeedsFromViceXml() {
        val test = Xmls.readFile(Xmls.VICE)
            .flatMap(tested::parseFeeds)
            .test()

        test.assertNoErrors()
        test.assertValueCount(1)
        val feeds = test.values()[0]
        Truth.assertThat(feeds[0]).isEqualTo(Xmls.VICE_FEED_0)
        Truth.assertThat(feeds[1]).isEqualTo(Xmls.VICE_FEED_1)
    }
}