package com.lelloman.read.feed

import com.google.common.truth.Truth.assertThat
import com.lelloman.read.core.TimeProvider
import com.nhaarman.mockito_kotlin.mock
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File

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
        val tester = readFile(MALFORMED_XML1)
            .flatMap(tested::parseFeeds)
            .test()

        tester.assertError { it is MalformedXmlException }
    }

    @Test
    fun `throws invalid root tag exception`() {
        val tester = readFile(INVALID_ROOT_TAG_XML)
            .flatMap(tested::parseFeeds)
            .test()

        tester.assertError { it is InvalidFeedTagException }
    }

    @Test
    fun `parses correct number of feeds from xmls`() {
        val test = Observable.fromIterable(ALL_RSS)
            .flatMapSingle(::readFile)
            .flatMapSingle(tested::parseFeeds)
            .test()

        test.assertValueCount(5)
        test.assertValueAt(0) { it.size == 23 }
        test.assertValueAt(1) { it.size == 118 }
        test.assertValueAt(2) { it.size == 3 }
        test.assertValueAt(3) { it.size == 10 }
        test.assertValueAt(4) { it.size == 30 }
    }

    @Test
    fun `parses feeds from sample xml`() {
        val test = readFile(SAMPLE)
            .flatMap(tested::parseFeeds)
            .test()

        test.assertNoErrors()
        test.assertValueCount(1)
        val feeds = test.values()[0]
        assertThat(feeds[0]).isEqualTo(SAMPLE_FEEDS[0])
        assertThat(feeds[1]).isEqualTo(SAMPLE_FEEDS[1])
        assertThat(feeds[2]).isEqualTo(SAMPLE_FEEDS[2])
    }

    private fun readFile(fileName: String): Single<String> = Single.fromCallable {
        val uri = javaClass.classLoader.getResource(fileName)
        File(uri.file).readText()
    }

    private companion object {
        const val FANPAGE = "rss_fanpage.xml"
        const val REPUBBLICA = "rss_repubblica.xml"
        const val SAMPLE = "rss_sample2.0.xml"
        const val VICE = "rss_vice.xml"
        const val WIRED = "rss_wired.xml"

        val ALL_RSS = listOf(FANPAGE, REPUBBLICA, SAMPLE, VICE, WIRED)

        const val MALFORMED_XML1 = "malformed_xml1.xml"
        const val INVALID_ROOT_TAG_XML = "invalid_root_tag.xml"

        val SAMPLE_FEEDS = listOf(
            Feed(
                title = "Star City",
                subtitle = "How do Americans get ready to work with Russians aboard the International\n" +
                    "                Space Station? They take a crash course in culture, language and protocol at\n" +
                    "                Russia's <a href=\"http://howe.iki.rssi.ru/GCTC/gctc_e.htm\">Star City</a>.\n            ",
                link = "http://liftoff.msfc.nasa.gov/news/2003/news-starcity.asp",
                timestamp = 1054633161000
            ),
            Feed(
                title = "The Engine That Does More",
                subtitle = "Before man travels to Mars, NASA hopes to design new engines that will let\n" +
                    "                us fly through the Solar System more quickly. The proposed VASIMR engine would do\n" +
                    "                that.\n" +
                    "            ",
                link = "http://liftoff.msfc.nasa.gov/news/2003/news-VASIMR.asp",
                timestamp = 1054024652000
            ),
            Feed(
                title = "Astronauts' Dirty Laundry",
                subtitle = "Compared to earlier spacecraft, the International Space Station has many\n" +
                    "                luxuries, but laundry facilities are not one of them. Instead, astronauts have other\n" +
                    "                options.\n" +
                    "            ",
                link = "http://liftoff.msfc.nasa.gov/news/2003/news-laundry.asp",
                timestamp = 1053420962000
            )
        )
    }
}