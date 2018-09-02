package com.lelloman.read.testutils.screen

import android.support.test.espresso.action.ViewActions
import com.lelloman.read.R
import com.lelloman.read.testutils.checkIsSwipeRefreshing
import com.lelloman.read.testutils.checkRecyclerViewCount
import com.lelloman.read.testutils.checkViewAtPositionHasText
import com.lelloman.read.testutils.clickOnRecyclerViewItem
import com.lelloman.read.testutils.clickViewWithText
import com.lelloman.read.testutils.openOverflowMenu
import com.lelloman.read.testutils.viewWithId

class ArticlesListScreen : Screen() {
    init {
        viewVisible(R.id.articles_recycler_view)
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

    fun clickOnDiscoverSourcesInOverflow(): DiscoverSourcesScreen {
        openOverflowMenu()
        clickViewWithText(string(R.string.discover_sources))
        return DiscoverSourcesScreen()
    }

    fun isSwipeRefreshing() = apply { checkIsSwipeRefreshing(true) }

    fun isNotSwipeRefreshing() = apply { checkIsSwipeRefreshing(false) }

    fun showsArticles(count: Int) = apply { checkRecyclerViewCount(count, R.id.articles_recycler_view) }

    fun swipeToRefresh() = apply { viewWithId(R.id.swipe_refresh_layout).perform(ViewActions.swipeDown()) }

    fun showsArticleWithTitle(position: Int, title: String) = apply { checkViewAtPositionHasText(position, title, R.id.articles_recycler_view) }

    fun rotateLeft() = apply { com.lelloman.read.testutils.rotateLeft() }
    fun rotateRight() = apply { com.lelloman.read.testutils.rotateRight() }
    fun rotateNatural() = apply { com.lelloman.read.testutils.rotateNatural() }

    fun clickOnArticle(position: Int) = with(clickOnRecyclerViewItem(position, R.id.articles_recycler_view)) {
        InAppArticleScreen()
    }
}