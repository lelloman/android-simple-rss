package com.lelloman.simplerss.testutils.screen

import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.lelloman.common.androidtestutils.Screen
import com.lelloman.common.androidtestutils.ViewActions.clickOnRecyclerViewItem
import com.lelloman.common.androidtestutils.ViewActions.clickViewWithText
import com.lelloman.common.androidtestutils.ViewActions.openOverflowMenu
import com.lelloman.common.androidtestutils.ViewAssertions.checkIsSwipeRefreshing
import com.lelloman.common.androidtestutils.ViewAssertions.checkRecyclerViewCount
import com.lelloman.common.androidtestutils.ViewAssertions.checkViewAtPositionHasImageGone
import com.lelloman.common.androidtestutils.ViewAssertions.checkViewAtPositionHasImageVisible
import com.lelloman.common.androidtestutils.ViewAssertions.checkViewAtPositionHasText
import com.lelloman.common.androidtestutils.viewWithId
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

    fun clickOnArticle(position: Int) = clickOnRecyclerViewItem(position, recyclerViewId).run {
        InAppArticleScreen()
    }

    fun articleWithImageAt(position: Int) = apply {
        checkViewAtPositionHasImageVisible(position, recyclerViewId, R.id.image)
    }

    fun articleWithoutImageAt(position: Int) = apply {
        checkViewAtPositionHasImageGone(position, recyclerViewId, R.id.image)
    }
}