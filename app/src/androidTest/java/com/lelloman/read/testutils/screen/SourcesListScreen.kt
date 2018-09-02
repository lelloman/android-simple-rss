package com.lelloman.read.testutils.screen

import android.support.test.espresso.Espresso.pressBack
import com.lelloman.read.R
import com.lelloman.read.testutils.checkRecyclerViewCount
import com.lelloman.read.testutils.clickView

class SourcesListScreen : Screen() {

    private val recyclerViewId = R.id.sources_recycler_view

    init {
        viewVisible(recyclerViewId)
    }

    fun clickAddSource(): AddSourceScreen {
        clickView(R.id.fab)
        return AddSourceScreen()
    }

    fun backToArticlesList() = with(pressBack()) { ArticlesListScreen() }

    fun showsSources(count: Int) = apply { checkRecyclerViewCount(count, recyclerViewId) }
}