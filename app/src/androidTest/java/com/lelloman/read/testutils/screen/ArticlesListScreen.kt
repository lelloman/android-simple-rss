package com.lelloman.read.testutils.screen

import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withText
import com.lelloman.read.R
import com.lelloman.read.testutils.checkIsSwipeRefreshing
import com.lelloman.read.testutils.checkRecyclerViewCount
import com.lelloman.read.testutils.checkViewAtPositionHasImageGone
import com.lelloman.read.testutils.checkViewAtPositionHasImageVisible
import com.lelloman.read.testutils.checkViewAtPositionHasText
import com.lelloman.read.testutils.clickOnRecyclerViewItem
import com.lelloman.read.testutils.clickViewWithText
import com.lelloman.read.testutils.openOverflowMenu
import com.lelloman.read.testutils.viewWithId

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

    fun isSwipeRefreshing() = apply { checkIsSwipeRefreshing(true) }

    fun isNotSwipeRefreshing() = apply { checkIsSwipeRefreshing(false) }

    fun showsArticles(count: Int) = apply { checkRecyclerViewCount(count, recyclerViewId) }

    fun swipeToRefresh() = apply { viewWithId(R.id.swipe_refresh_layout).perform(ViewActions.swipeDown()) }

    fun showsArticleWithTitle(position: Int, title: String) = apply { checkViewAtPositionHasText(position, title, recyclerViewId) }

    fun rotateLeft() = apply { com.lelloman.read.testutils.rotateLeft() }
    fun rotateRight() = apply { com.lelloman.read.testutils.rotateRight() }
    fun rotateNatural() = apply { com.lelloman.read.testutils.rotateNatural() }

    fun clickOnArticle(position: Int) = with(clickOnRecyclerViewItem(position, recyclerViewId)) {
        InAppArticleScreen()
    }

    fun articleWithImageAt(position: Int) = apply {
        checkViewAtPositionHasImageVisible(position, recyclerViewId, R.id.image)
    }

    fun articleWithoutImageAt(position: Int) = apply {
        checkViewAtPositionHasImageGone(position, recyclerViewId, R.id.image)
    }

    fun wait(seconds: Double) = apply { com.lelloman.read.testutils.wait(seconds) }
}