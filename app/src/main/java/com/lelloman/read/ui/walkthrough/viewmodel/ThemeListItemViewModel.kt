package com.lelloman.read.ui.walkthrough.viewmodel

import com.lelloman.read.core.viewmodel.BaseListItemViewModel
import com.lelloman.read.ui.walkthrough.ThemeListItem

class ThemeListItemViewModel : BaseListItemViewModel<ThemeListItem> {

    var name: String = ""
    private set

    var isSelected = false
    private set

    override fun bind(item: ThemeListItem) {
        name = item.theme.name
        isSelected = item.isEnabled
    }
}