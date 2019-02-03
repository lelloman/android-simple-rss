package com.lelloman.simplerss.testutils.screen

import android.support.test.espresso.Espresso.pressBack
import com.lelloman.instrumentedtestutils.Screen
import com.lelloman.instrumentedtestutils.checkRecyclerViewCount
import com.lelloman.instrumentedtestutils.viewWithTextIsDisplayed
import com.lelloman.simplerss.R

class FoundFeedsScreen : Screen() {

    init {
        viewVisible(R.id.found_feed_list_root)
    }

    fun wait(seconds: Int) = apply { com.lelloman.instrumentedtestutils.wait(seconds.toDouble()) }
    fun hasText(text: String) = apply { viewWithTextIsDisplayed(text) }

    fun backToDiscoverUrlScreen() = pressBack().run { DiscoverSourcesScreen() }

    fun displaysFoundFeeds(count: Int) = apply {
        checkRecyclerViewCount(count, R.id.discover_recycler_view)
    }
}