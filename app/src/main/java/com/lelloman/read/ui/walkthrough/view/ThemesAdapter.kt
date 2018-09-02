package com.lelloman.read.ui.walkthrough.view

import com.lelloman.read.R
import com.lelloman.read.core.view.BaseRecyclerViewAdapter
import com.lelloman.read.databinding.ListItemThemeBinding
import com.lelloman.read.ui.walkthrough.ThemeListItem
import com.lelloman.read.ui.walkthrough.viewmodel.ThemeListItemViewModel

class ThemesAdapter(
    onThemeClickedListener: (ThemeListItem) -> Unit
) : BaseRecyclerViewAdapter<ThemeListItem, ThemeListItemViewModel, ListItemThemeBinding>(
    onItemClickListener = onThemeClickedListener
) {
    override val listItemLayoutResId = R.layout.list_item_theme

    override fun bindViewModel(binding: ListItemThemeBinding, viewModel: ThemeListItemViewModel) {
        binding.viewModel = viewModel
    }

    override fun createViewModel(viewHolder: BaseViewHolder<ThemeListItem, ThemeListItemViewModel, ListItemThemeBinding>) =
        ThemeListItemViewModel()
}