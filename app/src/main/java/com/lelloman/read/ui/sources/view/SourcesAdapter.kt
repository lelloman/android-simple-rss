package com.lelloman.read.ui.sources.view

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.lelloman.read.R
import com.lelloman.read.core.SemanticTimeProvider
import com.lelloman.read.databinding.ListItemSourceBinding
import com.lelloman.read.persistence.db.model.Source
import com.lelloman.read.ui.sources.viewmodel.SourceListItemViewModel
import com.lelloman.read.utils.ModelWithIdListDiffCalculator

class SourcesAdapter(
    private val semanticTimeProvider: SemanticTimeProvider,
    private val onSourceClickedListener: (sourceId: Long) -> Unit,
    private val onSourceIsActiveChangedListener: (sourceId: Long, isActive: Boolean) -> Unit
) : RecyclerView.Adapter<SourcesAdapter.ViewHolder>(), Observer<List<Source>> {

    private var data = emptyList<Source>()
    private val listDiffCalculator = ModelWithIdListDiffCalculator()
    private val viewHolders = mutableListOf<ViewHolder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ListItemSourceBinding>(
            LayoutInflater.from(parent.context),
            R.layout.list_item_source,
            parent,
            false
        )
        return ViewHolder(binding).also { viewHolders.add(it) }
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    override fun onChanged(newData: List<Source>?) {
        newData?.let {
            val diff = listDiffCalculator.computeDiff(data, newData)
            this.data = newData
            diff.dispatchUpdatesTo(this)
        }
    }

    fun getItem(position: Int) = data[position]

    fun tick() = viewHolders.forEach { it.viewModel.tick() }

    inner class ViewHolder(private val binding: ListItemSourceBinding)
        : RecyclerView.ViewHolder(binding.root) {

        private lateinit var source: Source

        internal val viewModel = SourceListItemViewModel(
            semanticTimeProvider = semanticTimeProvider,
            onIsActiveChanged = { onSourceIsActiveChangedListener.invoke(source.id, it) }
        )

        init {
            binding.root.setOnClickListener { onSourceClickedListener.invoke(source.id) }
        }

        fun bind(source: Source) {
            this.source = source
            viewModel.bind(source)
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }
}