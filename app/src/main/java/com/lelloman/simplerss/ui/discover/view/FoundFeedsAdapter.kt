package com.lelloman.simplerss.ui.discover.view

import com.lelloman.common.view.ResourceProvider
import com.lelloman.common.view.adapter.BaseRecyclerViewAdapter
import com.lelloman.simplerss.R
import com.lelloman.simplerss.databinding.ListItemDiscoverFoundFeedBinding
import com.lelloman.simplerss.feed.finder.FoundFeed
import com.lelloman.simplerss.ui.discover.viewmodel.FoundFeedListItemViewModel

class FoundFeedsAdapter(
    private val resourceProvider: ResourceProvider,
    onFoundFeedClickListener: (FoundFeed) -> Unit
) : BaseRecyclerViewAdapter<Long, FoundFeed, FoundFeedListItemViewModel, ListItemDiscoverFoundFeedBinding>(
    onItemClickListener = onFoundFeedClickListener
) {
    override val listItemLayoutResId = R.layout.list_item_discover_found_feed

    override fun bindViewModel(binding: ListItemDiscoverFoundFeedBinding, viewModel: FoundFeedListItemViewModel) {
        binding.viewModel = viewModel
    }

    override fun createViewModel(viewHolder: BaseViewHolder<Long, FoundFeed, FoundFeedListItemViewModel, ListItemDiscoverFoundFeedBinding>) =
        FoundFeedListItemViewModel(resourceProvider)
}