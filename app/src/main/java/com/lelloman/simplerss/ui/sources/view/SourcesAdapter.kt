package com.lelloman.simplerss.ui.sources.view

import android.view.ViewGroup
import com.lelloman.common.view.ResourceProvider
import com.lelloman.common.view.SemanticTimeProvider
import com.lelloman.common.view.adapter.BaseRecyclerViewAdapter
import com.lelloman.simplerss.R
import com.lelloman.simplerss.databinding.ListItemSourceBinding

class SourcesAdapter(
    private val resourceProvider: ResourceProvider,
    private val semanticTimeProvider: SemanticTimeProvider,
    onSourceClickedListener: (source: com.lelloman.simplerss.persistence.db.model.Source) -> Unit,
    private val onSourceIsActiveChangedListener: (sourceId: Long, isActive: Boolean) -> Unit
) : BaseRecyclerViewAdapter<Long, com.lelloman.simplerss.persistence.db.model.Source, com.lelloman.simplerss.ui.sources.viewmodel.SourceListItemViewModel, ListItemSourceBinding>(
    onItemClickListener = onSourceClickedListener
) {

    private val viewHolders = mutableListOf<BaseViewHolder<Long, com.lelloman.simplerss.persistence.db.model.Source, com.lelloman.simplerss.ui.sources.viewmodel.SourceListItemViewModel, *>>()

    override val listItemLayoutResId = R.layout.list_item_source

    override fun bindViewModel(binding: ListItemSourceBinding, viewModel: com.lelloman.simplerss.ui.sources.viewmodel.SourceListItemViewModel) {
        binding.viewModel = viewModel
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        super.onCreateViewHolder(parent, viewType).also { viewHolders.add(it) }

    override fun createViewModel(viewHolder: BaseViewHolder<Long, com.lelloman.simplerss.persistence.db.model.Source, com.lelloman.simplerss.ui.sources.viewmodel.SourceListItemViewModel, ListItemSourceBinding>) = com.lelloman.simplerss.ui.sources.viewmodel.SourceListItemViewModel(
        resourceProvider = resourceProvider,
        semanticTimeProvider = semanticTimeProvider,
        onIsActiveChanged = { onSourceIsActiveChangedListener.invoke(viewHolder.item.id, it) }
    )

    fun tick() = viewHolders.forEach { it.viewModel.tick() }
}