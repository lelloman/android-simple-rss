package com.lelloman.read.testutils.screen

import android.support.test.espresso.Espresso.pressBack
import com.lelloman.read.R
import com.lelloman.read.testutils.checkRecyclerViewCount
import com.lelloman.read.testutils.viewWithTextIsDisplayed

class FoundFeedsScreen : Screen() {

    init {
        viewVisible(R.id.found_feed_list_root)
    }

    fun wait(seconds: Int) = apply { com.lelloman.read.testutils.wait(seconds.toDouble()) }
    fun hasText(text: String) = apply { viewWithTextIsDisplayed(text) }

    fun backToDiscoverUrlScreen() = with(pressBack()) { DiscoverSourcesScreen() }

    fun displaysFoundFeeds(count: Int) = apply {
        checkRecyclerViewCount(count, R.id.discover_recycler_view)
    }
}