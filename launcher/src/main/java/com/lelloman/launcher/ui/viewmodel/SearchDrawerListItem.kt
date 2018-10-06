package com.lelloman.launcher.ui.viewmodel

import com.lelloman.launcher.ui.AppsDrawerListItem

object SearchDrawerListItem : AppsDrawerListItem {
    override val id = Long.MIN_VALUE

    override val requiresFullRow = true

    override fun isFilteredOutBy(searchQuery: String) = false

}