package com.lelloman.read.screen

import android.support.test.espresso.Espresso.pressBack
import com.lelloman.read.R
import com.lelloman.read.testutils.viewWithTextIsDisplayed

class FoundFeedsScreen : Screen() {

    init {
        viewVisible(R.id.found_feed_list_root)
    }

    fun hasText(text: String) = apply { viewWithTextIsDisplayed(text) }

    fun backToDiscoverUrlScreen() = with(pressBack()) { DiscoverSourcesScreen() }
}