package com.lelloman.read.testutils.screen

import android.support.test.espresso.Espresso.pressBack
import com.lelloman.read.R
import com.lelloman.read.testutils.viewIsDisplayed

class AddSourceScreen : Screen() {
    init {
        viewIsDisplayed(R.id.add_source_root)
    }

    fun backToSourcesList() = with(pressBack()) { SourcesListScreen() }
}