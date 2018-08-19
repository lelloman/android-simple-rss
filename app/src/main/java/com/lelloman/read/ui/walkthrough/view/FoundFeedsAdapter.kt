package com.lelloman.read.ui.walkthrough.view

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.lelloman.read.R
import com.lelloman.read.core.ResourceProvider
import com.lelloman.read.databinding.ListItemDiscoverFoundFeedBinding
import com.lelloman.read.feed.finder.FoundFeed
import com.lelloman.read.ui.walkthrough.viewmodel.FoundFeedListItemViewModel
import com.lelloman.read.utils.ModelWithIdListDiffCalculator

class FoundFeedsAdapter(
    private val resourceProvider: ResourceProvider
) : RecyclerView.Adapter<FoundFeedsAdapter.ViewHolder>(), Observer<List<FoundFeed>> {

    private var data = emptyList<FoundFeed>()
    private val listDiffCalculator = ModelWithIdListDiffCalculator()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ListItemDiscoverFoundFeedBinding>(
            LayoutInflater.from(parent.context),
            R.layout.list_item_discover_found_feed,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    override fun onChanged(newData: List<FoundFeed>?) {
        newData?.let {
            val diff = listDiffCalculator.computeDiff(data, newData)
            this.data = newData
            diff.dispatchUpdatesTo(this)
        }
    }

    inner class ViewHolder(private val binding: ListItemDiscoverFoundFeedBinding)
        : RecyclerView.ViewHolder(binding.root) {

        private val viewModel = FoundFeedListItemViewModel(resourceProvider)

        fun bind(foundFeed: FoundFeed) {
            viewModel.bind(foundFeed)
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }
}