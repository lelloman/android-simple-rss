package com.lelloman.read.feed

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

    private val tested = FeedParser()

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
    fun test() {
        val tester = Observable
            .fromIterable(ALL_RSS)
            .map { javaClass.classLoader.getResource(it) }
            .map { File(it.file).readText() }
            .flatMapSingle { tested.parseFeeds(it) }
            .subscribe {
                println(it)
            }
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
    }
}