package com.lelloman.simplerss.testutils.screen

import androidx.test.espresso.Espresso.pressBack
import com.lelloman.common.androidtestutils.Screen
import com.lelloman.instrumentedtestutils.ViewAssertions.checkRecyclerViewCount
import com.lelloman.simplerss.R

class FoundFeedsScreen : Screen() {

    init {
        viewVisible(R.id.found_feed_list_root)
    }

    fun backToDiscoverUrlScreen() = pressBack().run { DiscoverSourcesScreen() }

    fun displaysFoundFeeds(count: Int) = apply {
        checkRecyclerViewCount(count, R.id.discover_recycler_view)
    }
}