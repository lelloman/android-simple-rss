package com.lelloman.launcher.ui

import com.lelloman.common.utils.model.ModelWithId

interface AppsDrawerListItem : ModelWithId {

    val requiresFullRow: Boolean

    fun isFilteredOutBy(searchQuery: String): Boolean

}