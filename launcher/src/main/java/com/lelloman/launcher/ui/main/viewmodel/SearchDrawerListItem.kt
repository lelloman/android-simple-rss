package com.lelloman.launcher.ui.main.viewmodel

import com.lelloman.launcher.ui.main.AppsDrawerListItem

object SearchDrawerListItem : AppsDrawerListItem {
    override val id = Long.MIN_VALUE

    override val requiresFullRow = true

    override fun isFilteredOutBy(searchQuery: String) = false
}