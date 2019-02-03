package com.lelloman.simplerss.ui.walkthrough.view

import com.lelloman.common.view.adapter.BaseRecyclerViewAdapter
import com.lelloman.simplerss.R
import com.lelloman.simplerss.databinding.ListItemThemeBinding

class ThemesAdapter(
    onThemeClickedListener: (com.lelloman.simplerss.ui.walkthrough.ThemeListItem) -> Unit
) : BaseRecyclerViewAdapter<Long, com.lelloman.simplerss.ui.walkthrough.ThemeListItem, com.lelloman.simplerss.ui.walkthrough.viewmodel.ThemeListItemViewModel, ListItemThemeBinding>(
    onItemClickListener = onThemeClickedListener
) {
    override val listItemLayoutResId = R.layout.list_item_theme

    override fun bindViewModel(binding: ListItemThemeBinding, viewModel: com.lelloman.simplerss.ui.walkthrough.viewmodel.ThemeListItemViewModel) {
        binding.viewModel = viewModel
    }

    override fun createViewModel(viewHolder: BaseViewHolder<Long, com.lelloman.simplerss.ui.walkthrough.ThemeListItem, com.lelloman.simplerss.ui.walkthrough.viewmodel.ThemeListItemViewModel, ListItemThemeBinding>) =
        com.lelloman.simplerss.ui.walkthrough.viewmodel.ThemeListItemViewModel()
}