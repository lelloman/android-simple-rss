package com.lelloman.read.testutils.screen

import android.support.test.espresso.Espresso.pressBack
import com.lelloman.read.R
import com.lelloman.read.testutils.viewIsDisplayed

class SettingsScreen : Screen() {
    init {
        viewIsDisplayed(R.id.settings_root)
    }

    fun backToArticlesList() = with(pressBack()) { ArticlesListScreen() }
}