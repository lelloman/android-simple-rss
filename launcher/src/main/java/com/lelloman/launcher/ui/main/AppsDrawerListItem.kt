package com.lelloman.launcher.ui.main

import com.lelloman.common.utils.model.ModelWithId

interface AppsDrawerListItem : ModelWithId<Long> {

    val requiresFullRow: Boolean

    fun isFilteredOutBy(searchQuery: String): Boolean

}