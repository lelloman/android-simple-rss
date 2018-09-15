package com.lelloman.read.ui.discover.view

import com.lelloman.common.view.BaseRecyclerViewAdapter
import com.lelloman.common.view.ResourceProvider
import com.lelloman.read.R
import com.lelloman.read.databinding.ListItemDiscoverFoundFeedBinding
import com.lelloman.read.feed.finder.FoundFeed
import com.lelloman.read.ui.discover.viewmodel.FoundFeedListItemViewModel

class FoundFeedsAdapter(
    private val resourceProvider: ResourceProvider,
    onFoundFeedClickListener: (FoundFeed) -> Unit
) : BaseRecyclerViewAdapter<FoundFeed, FoundFeedListItemViewModel, ListItemDiscoverFoundFeedBinding>(
    onItemClickListener = onFoundFeedClickListener
) {
    override val listItemLayoutResId = R.layout.list_item_discover_found_feed

    override fun bindViewModel(binding: ListItemDiscoverFoundFeedBinding, viewModel: FoundFeedListItemViewModel) {
        binding.viewModel = viewModel
    }

    override fun createViewModel(viewHolder: BaseViewHolder<FoundFeed, FoundFeedListItemViewModel, ListItemDiscoverFoundFeedBinding>) =
        FoundFeedListItemViewModel(resourceProvider)
}