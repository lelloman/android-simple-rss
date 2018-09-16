package com.lelloman.read.testutils.screen

import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withText
import com.lelloman.read.R
import com.lelloman.testutils.checkIsSwipeRefreshing
import com.lelloman.testutils.checkRecyclerViewCount
import com.lelloman.testutils.checkViewAtPositionHasImageGone
import com.lelloman.testutils.checkViewAtPositionHasImageVisible
import com.lelloman.testutils.checkViewAtPositionHasText
import com.lelloman.testutils.clickOnRecyclerViewItem
import com.lelloman.testutils.clickViewWithText
import com.lelloman.testutils.openOverflowMenu
import com.lelloman.testutils.viewWithId

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

    fun rotateLeft() = apply { com.lelloman.testutils.rotateLeft() }
    fun rotateRight() = apply { com.lelloman.testutils.rotateRight() }
    fun rotateNatural() = apply { com.lelloman.testutils.rotateNatural() }

    fun clickOnArticle(position: Int) = with(clickOnRecyclerViewItem(position, recyclerViewId)) {
        InAppArticleScreen()
    }

    fun articleWithImageAt(position: Int) = apply {
        checkViewAtPositionHasImageVisible(position, recyclerViewId, R.id.image)
    }

    fun articleWithoutImageAt(position: Int) = apply {
        checkViewAtPositionHasImageGone(position, recyclerViewId, R.id.image)
    }

    fun wait(seconds: Double) = apply { com.lelloman.testutils.wait(seconds) }
}