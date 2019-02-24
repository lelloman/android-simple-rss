package com.lelloman.simplerss.testutils.screen

import androidx.test.espresso.Espresso.pressBack
import com.lelloman.common.androidtestutils.Screen
import com.lelloman.instrumentedtestutils.ViewAssertions.checkViewIsDisplayed
import com.lelloman.simplerss.R

class InAppArticleScreen : Screen() {
    init {
        checkViewIsDisplayed(R.id.web_view)
    }

    fun backToArticlesList() = pressBack().run { ArticlesListScreen() }
}