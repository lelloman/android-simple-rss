package com.lelloman.read.ui.walkthrough.view

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.lelloman.read.R
import com.lelloman.read.databinding.ListItemThemeBinding
import com.lelloman.read.ui.walkthrough.ThemeListItem
import com.lelloman.read.ui.walkthrough.viewmodel.ThemeListItemViewModel
import com.lelloman.read.utils.ModelWithIdListDiffCalculator

class ThemesAdapter(
    private val onThemeClickedListener: (ThemeListItem) -> Unit
) : RecyclerView.Adapter<ThemesAdapter.ViewHolder>(),
    Observer<List<ThemeListItem>> {

    private var data = emptyList<ThemeListItem>()
    private val listDiffCalculator = ModelWithIdListDiffCalculator()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ListItemThemeBinding>(
            LayoutInflater.from(parent.context),
            R.layout.list_item_theme,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    override fun onChanged(newData: List<ThemeListItem>?) {
        newData?.let {
            val diff = listDiffCalculator.computeDiff(data, newData)
            this.data = newData
            diff.dispatchUpdatesTo(this)
        }
    }

    inner class ViewHolder(private val binding: ListItemThemeBinding)
        : RecyclerView.ViewHolder(binding.root) {

        private val viewModel = ThemeListItemViewModel()
        private lateinit var theme: ThemeListItem

        init {
            binding.root.setOnClickListener {
                onThemeClickedListener.invoke(theme)
            }
        }

        fun bind(theme: ThemeListItem) {
            this.theme = theme
            this.viewModel.bind(theme)
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }
}