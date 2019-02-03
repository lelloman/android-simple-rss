package com.lelloman.simplerss.ui.walkthrough.view

import com.lelloman.common.view.adapter.BaseRecyclerViewAdapter
import com.lelloman.simplerss.R
import com.lelloman.simplerss.databinding.ListItemThemeBinding
import com.lelloman.simplerss.ui.walkthrough.ThemeListItem
import com.lelloman.simplerss.ui.walkthrough.viewmodel.ThemeListItemViewModel

class ThemesAdapter(
    onThemeClickedListener: (ThemeListItem) -> Unit
) : BaseRecyclerViewAdapter<Long, ThemeListItem, ThemeListItemViewModel, ListItemThemeBinding>(
    onItemClickListener = onThemeClickedListener
) {
    override val listItemLayoutResId = R.layout.list_item_theme

    override fun bindViewModel(binding: ListItemThemeBinding, viewModel: ThemeListItemViewModel) {
        binding.viewModel = viewModel
    }

    override fun createViewModel(viewHolder: BaseViewHolder<Long, ThemeListItem, ThemeListItemViewModel, ListItemThemeBinding>) =
        ThemeListItemViewModel()
}