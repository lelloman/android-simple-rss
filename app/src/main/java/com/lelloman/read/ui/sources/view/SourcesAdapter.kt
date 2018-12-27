package com.lelloman.read.ui.sources.view

import android.view.ViewGroup
import com.lelloman.common.view.adapter.BaseRecyclerViewAdapter
import com.lelloman.common.view.ResourceProvider
import com.lelloman.common.view.SemanticTimeProvider
import com.lelloman.read.R
import com.lelloman.read.databinding.ListItemSourceBinding
import com.lelloman.read.persistence.db.model.Source
import com.lelloman.read.ui.sources.viewmodel.SourceListItemViewModel

class SourcesAdapter(
    private val resourceProvider: ResourceProvider,
    private val semanticTimeProvider: SemanticTimeProvider,
    onSourceClickedListener: (source: Source) -> Unit,
    private val onSourceIsActiveChangedListener: (sourceId: Long, isActive: Boolean) -> Unit
) : BaseRecyclerViewAdapter<Source, SourceListItemViewModel, ListItemSourceBinding>(
    onItemClickListener = onSourceClickedListener
) {

    private val viewHolders = mutableListOf<BaseViewHolder<Source, SourceListItemViewModel, *>>()

    override val listItemLayoutResId = R.layout.list_item_source

    override fun bindViewModel(binding: ListItemSourceBinding, viewModel: SourceListItemViewModel) {
        binding.viewModel = viewModel
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        super.onCreateViewHolder(parent, viewType).also { viewHolders.add(it) }

    override fun createViewModel(viewHolder: BaseViewHolder<Source, SourceListItemViewModel, ListItemSourceBinding>) = SourceListItemViewModel(
        resourceProvider = resourceProvider,
        semanticTimeProvider = semanticTimeProvider,
        onIsActiveChanged = { onSourceIsActiveChangedListener.invoke(viewHolder.item.id, it) }
    )

    fun tick() = viewHolders.forEach { it.viewModel.tick() }
}