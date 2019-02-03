package com.lelloman.simplerss.ui.walkthrough.viewmodel

import com.lelloman.common.viewmodel.BaseListItemViewModel

class ThemeListItemViewModel : BaseListItemViewModel<Long, com.lelloman.simplerss.ui.walkthrough.ThemeListItem> {

    var name: String = ""
        private set

    var isSelected = false
        private set

    override fun bind(item: com.lelloman.simplerss.ui.walkthrough.ThemeListItem) {
        name = item.theme.name
        isSelected = item.isEnabled
    }
}