package com.lelloman.read.ondemand

import com.lelloman.read.feed.finder.FeedFinder
import com.lelloman.read.testutils.BagOfDependencies
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

class FeedFinderTest {

    private lateinit var tested: FeedFinder

    @Before
    fun setUp() {
        val dependencies = BagOfDependencies()
        tested = dependencies.feedFinder
    }

    @Ignore
    @Test
    fun findsFanpageFeedUrl() {
        val tester = tested.findValidFeedUrls("http://www.fanpage.it").test()

        tester.assertValueCount(3)
    }

    @Ignore
    @Test
    fun findsRepubblicaFeedUrl() {
        val tester = tested.findValidFeedUrls("http://www.repubblica.it").test()

        tester.assertValueCount(0)
    }

    @Ignore
    @Test
    fun findsAnsaFeedUrl() {
        val tester = tested.findValidFeedUrls("http://www.ansa.it").test()

        tester.assertValueCount(43)
    }

    @Ignore
    @Test
    fun findsAndroidWeeklyFeedUrl() {
        val tester = tested.findValidFeedUrls("http://www.androidweekly.net").test()

        tester.assertValueCount(1)
    }

    @Ignore
    @Test
    fun findsIlMattinoFeedUrl() {
        val tester = tested.findValidFeedUrls("http://www.ilmattino.it").test()

        tester.assertValueCount(1)
    }

    @Ignore
    @Test
    fun findsViceFeedUrl() {
        val tester = tested.findValidFeedUrls("http://www.vice.com/it").test()

        tester.assertValueCount(2)
    }

    @Ignore
    @Test
    fun findsWiredFeedUrl() {
        val tester = tested.findValidFeedUrls("http://www.wired.it").test()

        tester.assertValueCount(2)
    }

    @Ignore
    @Test
    fun findsAndroidDevBlogFeedUrl() {
        val tester = tested.findValidFeedUrls("https://android-developers.googleblog.com").test()

        tester.assertValueCount(0)
    }
}