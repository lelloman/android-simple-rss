package com.lelloman.launcher.ui.main.viewmodel

import com.lelloman.common.utils.model.ModelWithId
import com.lelloman.launcher.packages.Package
import com.lelloman.launcher.ui.main.AppsDrawerListItem

data class PackageDrawerListItem(
    val pkg: Package
) : AppsDrawerListItem, ModelWithId by pkg {

    override val requiresFullRow = false

    override fun isFilteredOutBy(searchQuery: String) =
        !pkg.label.contains(searchQuery, true) &&
            !pkg.packageName.contains(searchQuery, true)
}