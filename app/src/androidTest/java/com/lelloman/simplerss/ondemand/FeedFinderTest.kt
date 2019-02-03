package com.lelloman.simplerss.ondemand

import com.lelloman.simplerss.testutils.BagOfDependencies
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

@Ignore
class FeedFinderTest {

    private lateinit var tested: com.lelloman.simplerss.feed.finder.FeedFinder

    @Before
    fun setUp() {
        val dependencies = BagOfDependencies()
        tested = dependencies.feedFinder
    }

    @Test
    fun findsFanpageFeedUrl() {
        val tester = tested.findValidFeedUrls("http://www.fanpage.it").test()

        tester.assertValueCount(3)
    }

    @Test
    fun findsRepubblicaFeedUrl() {
        val tester = tested.findValidFeedUrls("http://www.repubblica.it").test()

        tester.assertValueCount(0)
    }

    @Test
    fun findsAnsaFeedUrl() {
        val tester = tested.findValidFeedUrls("http://www.ansa.it").test()

        tester.assertValueCount(43)
    }

    @Test
    fun findsAndroidWeeklyFeedUrl() {
        val tester = tested.findValidFeedUrls("http://www.androidweekly.net").test()

        tester.assertValueCount(1)
    }

    @Test
    fun findsIlMattinoFeedUrl() {
        val tester = tested.findValidFeedUrls("http://www.ilmattino.it").test()

        tester.assertValueCount(1)
    }

    @Test
    fun findsViceFeedUrl() {
        val tester = tested.findValidFeedUrls("http://www.vice.com/it").test()

        tester.assertValueCount(2)
    }

    @Test
    fun findsWiredFeedUrl() {
        val tester = tested.findValidFeedUrls("http://www.wired.it").test()

        tester.assertValueCount(2)
    }

    @Test
    fun findsAndroidDevBlogFeedUrl() {
        val tester = tested.findValidFeedUrls("https://android-developers.googleblog.com").test()

        tester.assertValueCount(1)
    }
}