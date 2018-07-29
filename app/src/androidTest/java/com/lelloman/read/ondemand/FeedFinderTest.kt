package com.lelloman.read.ondemand

import com.lelloman.read.feed.FeedFinder
import com.lelloman.read.testutils.BagOfDependencies
import org.junit.Before
import org.junit.Test

class FeedFinderTest {

    private lateinit var tested: FeedFinder

    @Before
    fun setUp() {
        val dependencies = BagOfDependencies()
        tested = dependencies.feedFinder
    }

    //@Ignore
    @Test
    fun findsFanpageFeedUrl() {
        val tester = tested.findValidFeedUrls("http://www.fanpage.it").test()

        tester.assertValueCount(4)
    }

    //@Ignore
    @Test
    fun findsRepubblicaFeedUrl() {
        val tester = tested.findValidFeedUrls("http://www.repubblica.it").test()

        tester.assertValueCount(0)
    }

    //@Ignore
    @Test
    fun findsAnsaFeedUrl() {
        val tester = tested.findValidFeedUrls("http://www.ansa.it").test()

        tester.assertValueCount(0)
    }

    //@Ignore
    @Test
    fun findsAndroidWeeklyFeedUrl() {
        val tester = tested.findValidFeedUrls("http://www.androidweekly.net").test()

        tester.assertValueCount(1)
    }
}