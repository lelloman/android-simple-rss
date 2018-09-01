package com.lelloman.read.ui.walkthrough.viewmodel

import com.lelloman.read.ui.walkthrough.ThemeListItem

class ThemeListItemViewModel {

    var name: String = ""
    private set

    var isSelected = false
    private set

    fun bind(theme: ThemeListItem) {
        name = theme.theme.name
        isSelected = theme.isEnabled
    }
}