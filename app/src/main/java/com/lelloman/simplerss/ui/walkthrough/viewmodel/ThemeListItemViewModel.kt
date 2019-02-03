package com.lelloman.simplerss.ui.walkthrough.viewmodel

import com.lelloman.common.view.AppTheme
import com.lelloman.common.viewmodel.BaseListItemViewModel
import com.lelloman.simplerss.ui.walkthrough.ThemeListItem

class ThemeListItemViewModel(private val onItemClickListener: (ThemeListItem) -> Unit)
    : BaseListItemViewModel<Long, ThemeListItem> {

    private lateinit var item: ThemeListItem

    var appTheme: AppTheme? = null
        private set

    var name: String = ""
        private set

    var isSelected = false
        private set

    override fun bind(item: ThemeListItem) {
        this.item = item
        name = item.theme.name
        isSelected = item.isEnabled
        appTheme = item.theme
    }

    fun onItemClicked() = onItemClickListener(item)
}