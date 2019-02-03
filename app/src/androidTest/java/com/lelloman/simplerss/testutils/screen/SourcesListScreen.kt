package com.lelloman.simplerss.testutils.screen

import android.support.test.espresso.Espresso.pressBack
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import com.lelloman.instrumentedtestutils.Screen
import com.lelloman.instrumentedtestutils.ViewActions.clickView
import com.lelloman.instrumentedtestutils.ViewActions.clickViewWithText
import com.lelloman.instrumentedtestutils.ViewAssertions.checkRecyclerViewCount
import com.lelloman.instrumentedtestutils.ViewAssertions.checkViewIsDisplayed
import com.lelloman.instrumentedtestutils.ViewAssertions.checkViewWithTextIsDisplayed
import com.lelloman.instrumentedtestutils.checkMatches
import com.lelloman.instrumentedtestutils.viewWithText
import com.lelloman.simplerss.R
import org.hamcrest.CoreMatchers.not

class SourcesListScreen : Screen() {

    private val recyclerViewId = R.id.sources_recycler_view

    init {
        viewVisible(recyclerViewId)
    }

    fun clickAddSource(): AddSourceScreen {
        clickView(R.id.add_source_fab)
        return AddSourceScreen()
    }

    fun backToArticlesList() = pressBack().run { ArticlesListScreen() }

    fun showsSources(count: Int) = apply { checkRecyclerViewCount(count, recyclerViewId) }

    fun clickOnSource(text: String) = apply { clickViewWithText(text) }

    fun showsFab() = apply { checkViewIsDisplayed(R.id.add_source_fab) }

    fun showsEmptyListMessage() = apply { checkViewWithTextIsDisplayed(string(R.string.sources_list_empty_list_message)) }

    fun doesntShowEmptyListMessage() = apply { viewWithText(string(R.string.sources_list_empty_list_message)).checkMatches(not(isDisplayed())) }
}