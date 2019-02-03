package com.lelloman.simplerss.testutils.screen

import android.support.test.espresso.Espresso.pressBack
import com.lelloman.instrumentedtestutils.Screen
import com.lelloman.instrumentedtestutils.viewIsDisplayed
import com.lelloman.simplerss.R

class InAppArticleScreen : Screen() {
    init {
        viewIsDisplayed(R.id.web_view)
    }

    fun backToArticlesList() = pressBack().run { ArticlesListScreen() }
}