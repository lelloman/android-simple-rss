package com.lelloman.read.testutils.screen

import android.support.test.espresso.Espresso.pressBack
import com.lelloman.read.R
import com.lelloman.testutils.Screen
import com.lelloman.testutils.viewIsDisplayed

class InAppArticleScreen : Screen() {
    init {
        viewIsDisplayed(R.id.web_view)
    }

    fun backToArticlesList() = pressBack().run { ArticlesListScreen() }
}