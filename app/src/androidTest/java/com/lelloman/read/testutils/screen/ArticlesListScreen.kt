package com.lelloman.read.testutils.screen

import com.lelloman.read.R
import com.lelloman.read.testutils.clickViewWithText
import com.lelloman.read.testutils.openOverflowMenu

class ArticlesListScreen : Screen() {
    init {
        viewVisible(R.id.articles_recycler_view)
    }

    fun openOverflow() = apply { openOverflowMenu() }

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
}