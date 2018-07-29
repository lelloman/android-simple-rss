package com.lelloman.read.feed

import com.google.common.truth.Truth.assertThat
import com.lelloman.read.core.TimeProvider
import com.lelloman.read.feed.exception.InvalidFeedTagException
import com.lelloman.read.feed.exception.MalformedXmlException
import com.lelloman.read.testutils.Xmls
import com.nhaarman.mockito_kotlin.mock
import io.reactivex.Observable
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class FeedParserTest {

    private val time = 0L
    private val timeProvider: TimeProvider = mock {
        on { nowUtcMs() }.thenAnswer { time }
    }

    private val tested = FeedParser(timeProvider)

    @Test
    fun `throws malformed xml exception`() {
        val tester = Xmls.readFile(Xmls.MALFORMED_XML1)
            .flatMap(tested::parseFeeds)
            .test()

        tester.assertError { it is MalformedXmlException }
    }

    @Test
    fun `throws invalid root tag exception`() {
        val tester = Xmls.readFile(Xmls.INVALID_ROOT_TAG_XML)
            .flatMap(tested::parseFeeds)
            .test()

        tester.assertError { it is InvalidFeedTagException }
    }

    @Test
    fun `parses correct number of feeds from xmls`() {
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
    fun `parses feeds from sample xml`() {
        val tester = Xmls.readFile(Xmls.SAMPLE)
            .flatMap(tested::parseFeeds)
            .test()

        tester.assertNoErrors()
        tester.assertValueCount(1)
        val feeds = tester.values()[0]
        assertThat(feeds[0]).isEqualTo(Xmls.SAMPLE_FEEDS[0])
        assertThat(feeds[1]).isEqualTo(Xmls.SAMPLE_FEEDS[1])
        assertThat(feeds[2]).isEqualTo(Xmls.SAMPLE_FEEDS[2])
    }

    @Test
    fun `parses feeds from vice xml`() {
        val test = Xmls.readFile(Xmls.VICE)
            .flatMap(tested::parseFeeds)
            .test()

        test.assertNoErrors()
        test.assertValueCount(1)
        val feeds = test.values()[0]
        assertThat(feeds[0]).isEqualTo(Xmls.VICE_FEED_0)
        assertThat(feeds[1]).isEqualTo(Xmls.VICE_FEED_1)
    }
}