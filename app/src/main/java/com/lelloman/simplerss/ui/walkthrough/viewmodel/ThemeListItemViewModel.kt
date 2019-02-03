package com.lelloman.simplerss.ui.walkthrough.viewmodel

import com.lelloman.common.viewmodel.BaseListItemViewModel
import com.lelloman.simplerss.ui.walkthrough.ThemeListItem

class ThemeListItemViewModel : BaseListItemViewModel<Long, ThemeListItem> {

    var name: String = ""
        private set

    var isSelected = false
        private set

    override fun bind(item: ThemeListItem) {
        name = item.theme.name
        isSelected = item.isEnabled
    }
}