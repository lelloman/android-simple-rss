package com.lelloman.read.testutils.screen

import android.support.test.espresso.Espresso.pressBack
import com.lelloman.read.R
import com.lelloman.read.testutils.checkRecyclerViewCount
import com.lelloman.read.testutils.clickView

class SourcesListScreen : Screen() {

    init {
        viewVisible(R.id.sources_recycler_view)
    }

    fun clickAddSource(): AddSourceScreen {
        clickView(R.id.fab)
        return AddSourceScreen()
    }

    fun backToArticlesList() = with(pressBack()) { ArticlesListScreen() }

    fun showsSources(count: Int) = apply { checkRecyclerViewCount(count, R.id.sources_recycler_view) }
}