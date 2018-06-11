package com.lelloman.read.sources.view

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.lelloman.read.R
import com.lelloman.read.databinding.ListItemSourceBinding
import com.lelloman.read.persistence.model.Source
import com.lelloman.read.sources.viewmodel.SourceViewModel
import com.lelloman.read.utils.ModelWithIdListDiffCalculator
import javax.inject.Inject

class SourcesAdapter @Inject constructor()
    : RecyclerView.Adapter<SourcesAdapter.ViewHolder>(), Observer<List<Source>> {

    private var data = emptyList<Source>()
    private val listDiffCalculator = ModelWithIdListDiffCalculator()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ListItemSourceBinding>(
            LayoutInflater.from(parent.context),
            R.layout.list_item_source,
            parent,
            false
        )
        return ViewHolder(binding)
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

    class ViewHolder(private val binding: ListItemSourceBinding)
        : RecyclerView.ViewHolder(binding.root) {

        private val viewModel = SourceViewModel()

        fun bind(source: Source) {
            viewModel.bind(source)
            binding.source = viewModel
            binding.executePendingBindings()
        }
    }
}