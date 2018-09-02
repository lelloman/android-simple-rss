package com.lelloman.read.testutils.screen

import android.support.test.espresso.Espresso.pressBack
import com.lelloman.read.R
import com.lelloman.read.testutils.viewIsDisplayed

class InAppArticleScreen : Screen() {
    init {
        viewIsDisplayed(R.id.web_view)
    }

    fun backToArticlesList() = with(pressBack()) { ArticlesListScreen() }
}