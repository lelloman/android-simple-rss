package com.lelloman.simplerss.testutils.screen

import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.lelloman.instrumentedtestutils.Screen
import com.lelloman.instrumentedtestutils.checkIsSwipeRefreshing
import com.lelloman.instrumentedtestutils.checkRecyclerViewCount
import com.lelloman.instrumentedtestutils.checkViewAtPositionHasImageGone
import com.lelloman.instrumentedtestutils.checkViewAtPositionHasImageVisible
import com.lelloman.instrumentedtestutils.checkViewAtPositionHasText
import com.lelloman.instrumentedtestutils.clickOnRecyclerViewItem
import com.lelloman.instrumentedtestutils.clickViewWithText
import com.lelloman.instrumentedtestutils.openOverflowMenu
import com.lelloman.instrumentedtestutils.viewWithId
import com.lelloman.simplerss.R

class ArticlesListScreen : Screen() {

    private val recyclerViewId = R.id.articles_recycler_view

    init {
        viewVisible(recyclerViewId)
    }

    fun clickOnSourcesInOverflow(): SourcesListScreen {
        openOverflowMenu()
        clickViewWithText(string(R.string.sources))
        return SourcesListScreen()
    }

    fun clickOnSettingsInOverflow(): SettingsScreen {
        openOverflowMenu()
        clickViewWithText(string(R.string.settings))
        return SettingsScreen()
    }

    fun showsEmptyViewWithNoSourcesText() = apply { showsEmptyViewText(string(R.string.empty_articles_no_source)) }

    fun showsEmptyViewWithNoArticlesText() = apply { showsEmptyViewText(string(R.string.empty_articles_must_refresh)) }

    private fun showsEmptyViewText(text: String) = apply {
        viewWithId(R.id.text_view_empty_list).check(matches(withText(text)))
    }

    fun clickOnDiscoverSourcesInOverflow(): DiscoverSourcesScreen {
        openOverflowMenu()
        clickViewWithText(string(R.string.discover_sources))
        return DiscoverSourcesScreen()
    }

    fun isSwipeRefreshing() = apply { checkIsSwipeRefreshing(true, R.id.swipe_refresh_layout) }

    fun isNotSwipeRefreshing() = apply { checkIsSwipeRefreshing(false, R.id.swipe_refresh_layout) }

    fun showsArticles(count: Int) = apply { checkRecyclerViewCount(count, recyclerViewId) }

    fun swipeToRefresh() = apply { viewWithId(R.id.swipe_refresh_layout).perform(ViewActions.swipeDown()) }

    fun showsArticleWithTitle(position: Int, title: String) = apply { checkViewAtPositionHasText(position, title, recyclerViewId) }

    fun rotateLeft() = apply { com.lelloman.instrumentedtestutils.rotateLeft() }
    fun rotateRight() = apply { com.lelloman.instrumentedtestutils.rotateRight() }
    fun rotateNatural() = apply { com.lelloman.instrumentedtestutils.rotateNatural() }

    fun clickOnArticle(position: Int) = clickOnRecyclerViewItem(position, recyclerViewId).run {
        InAppArticleScreen()
    }

    fun articleWithImageAt(position: Int) = apply {
        checkViewAtPositionHasImageVisible(position, recyclerViewId, R.id.image)
    }

    fun articleWithoutImageAt(position: Int) = apply {
        checkViewAtPositionHasImageGone(position, recyclerViewId, R.id.image)
    }

    fun wait(seconds: Double) = apply { com.lelloman.instrumentedtestutils.wait(seconds) }
}