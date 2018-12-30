package com.lelloman.read.testutils.screen

import android.support.test.espresso.Espresso.pressBack
import com.lelloman.instrumentedtestutils.Screen
import com.lelloman.instrumentedtestutils.checkRecyclerViewCount
import com.lelloman.instrumentedtestutils.clickView
import com.lelloman.instrumentedtestutils.clickViewWithText
import com.lelloman.read.R

class SourcesListScreen : Screen() {

    private val recyclerViewId = R.id.sources_recycler_view

    init {
        viewVisible(recyclerViewId)
    }

    fun clickAddSource(): AddSourceScreen {
        clickView(R.id.fab)
        return AddSourceScreen()
    }

    fun backToArticlesList() = pressBack().run { ArticlesListScreen() }

    fun showsSources(count: Int) = apply { checkRecyclerViewCount(count, recyclerViewId) }

    fun clickOnSource(text: String) = apply { clickViewWithText(text) }
}