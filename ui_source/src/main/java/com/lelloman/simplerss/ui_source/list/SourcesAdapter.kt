package com.lelloman.simplerss.ui_source.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lelloman.simplerss.ui_source.databinding.ListItemSourceBinding
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class SourcesAdapter : RecyclerView.Adapter<SourcesAdapter.ViewHolder>() {

    private var data = emptyList<SourcesViewModel.ListItem>()

    private val itemClickSubject = PublishSubject.create<SourcesViewModel.ListItem>()
    val itemClick: Observable<SourcesViewModel.ListItem> = itemClickSubject.hide()

    fun onNewItems(newItems: List<SourcesViewModel.ListItem>) {
        val diff = DiffUtil.calculateDiff(DiffCallback(data, newItems))
        data = newItems
        diff.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = LayoutInflater.from(parent.context)
        .let { ListItemSourceBinding.inflate(it, parent, false) }
        .let { ViewHolder(it) }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    override fun getItemCount() = data.size

    private class DiffCallback(
        private val oldList: List<SourcesViewModel.ListItem>,
        private val newList: List<SourcesViewModel.ListItem>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].id == newList[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]
    }

    inner class ViewHolder(private val binding: ListItemSourceBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SourcesViewModel.ListItem) {
            binding.root.setOnClickListener { itemClickSubject.onNext(item) }
            binding.nameTextView.text = item.name
        }
    }
}