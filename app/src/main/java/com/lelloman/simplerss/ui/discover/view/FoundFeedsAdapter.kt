package com.lelloman.simplerss.ui.discover.view

import com.lelloman.common.view.ResourceProvider
import com.lelloman.common.view.adapter.BaseRecyclerViewAdapter
import com.lelloman.simplerss.R
import com.lelloman.simplerss.databinding.ListItemDiscoverFoundFeedBinding

class FoundFeedsAdapter(
    private val resourceProvider: ResourceProvider,
    onFoundFeedClickListener: (com.lelloman.simplerss.feed.finder.FoundFeed) -> Unit
) : BaseRecyclerViewAdapter<Long, com.lelloman.simplerss.feed.finder.FoundFeed, com.lelloman.simplerss.ui.discover.viewmodel.FoundFeedListItemViewModel, ListItemDiscoverFoundFeedBinding>(
    onItemClickListener = onFoundFeedClickListener
) {
    override val listItemLayoutResId = R.layout.list_item_discover_found_feed

    override fun bindViewModel(binding: ListItemDiscoverFoundFeedBinding, viewModel: com.lelloman.simplerss.ui.discover.viewmodel.FoundFeedListItemViewModel) {
        binding.viewModel = viewModel
    }

    override fun createViewModel(viewHolder: BaseViewHolder<Long, com.lelloman.simplerss.feed.finder.FoundFeed, com.lelloman.simplerss.ui.discover.viewmodel.FoundFeedListItemViewModel, ListItemDiscoverFoundFeedBinding>) =
        com.lelloman.simplerss.ui.discover.viewmodel.FoundFeedListItemViewModel(resourceProvider)
}