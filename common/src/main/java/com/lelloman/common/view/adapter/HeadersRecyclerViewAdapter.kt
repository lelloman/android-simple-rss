package com.lelloman.common.view.adapter

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.lelloman.common.R
import com.lelloman.common.utils.ModelWithIdListDiffCalculator
import com.lelloman.common.utils.model.ModelWithId
import com.lelloman.common.viewmodel.BaseListItemViewModel
import java.util.*

abstract class HeadersRecyclerViewAdapter<ID, M : ModelWithId<ID>, VM : BaseListItemViewModel<ID, M>, DB : ViewDataBinding>(
    private val onItemClickListener: ((M) -> Unit)? = null
) : RecyclerView.Adapter<HeadersRecyclerViewAdapter<ID, M, VM, DB>.ViewHolder>(), Observer<List<M>> {

    private var data = emptyList<M>()
    private var dataWithHeaders = emptyList<ModelWithId<ID>>()

    private val listDiffCalculator = ModelWithIdListDiffCalculator<ID>()

    abstract val listItemLayoutResId: Int

    protected abstract fun bindViewModel(binding: DB, viewModel: VM)

    protected abstract fun createViewModel(): VM

    protected abstract fun getHeaderId(item: M): ID

    protected abstract fun getHeaderText(item: M): String

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = dataWithHeaders[position]

        when (viewHolder) {
            is ItemViewHolder -> viewHolder.bind((item as ItemHolder<ID, M>).item)
            is HeaderViewHolder -> viewHolder.bind(item as HeaderHolder)
        }
    }

    override fun onChanged(newData: List<M>?) {
        if (newData == null) return

        val newDataWithHeaders = LinkedList<ModelWithId<ID>>()
        var prevHeaderId: ID? = null
        newData.forEach {
            val headerId = getHeaderId(it)
            if (headerId != prevHeaderId) {
                prevHeaderId = headerId
                val headerText = getHeaderText(it)
                newDataWithHeaders.add(HeaderHolder(headerId, headerText))
            }
            newDataWithHeaders.add(ItemHolder(it))
        }

        val diff = listDiffCalculator.computeDiff(dataWithHeaders, newDataWithHeaders)
        this.dataWithHeaders = newDataWithHeaders
        this.data = newData
        diff.dispatchUpdatesTo(this)
    }

    override fun getItemViewType(position: Int) = when (dataWithHeaders[position]) {
        is ItemHolder<*, *> -> VIEW_TYPE_ITEM
        is HeaderHolder -> VIEW_TYPE_HEADER
        else -> throw IllegalStateException("Item at position $position is neither ItemHolder nor HeaderHolder.")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        VIEW_TYPE_HEADER -> makeHeaderViewHolder(parent)
        VIEW_TYPE_ITEM -> makeItemViewHolder(parent)
        else -> throw IllegalStateException("Cannot create ViewHolder for unknown view type $viewType.")
    }

    private fun makeHeaderViewHolder(parent: ViewGroup) = LayoutInflater
        .from(parent.context)
        .inflate(R.layout.list_item_header, parent, false)
        .let(::HeaderViewHolder)

    private fun makeItemViewHolder(parent: ViewGroup): ItemViewHolder {
        val binding: DB = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            listItemLayoutResId,
            parent,
            false
        )

        return ItemViewHolder(
            binding = binding,
            viewModelFactory = ::createViewModel,
            viewModelBinder = ::bindViewModel
        )
    }

    override fun getItemCount() = dataWithHeaders.size

    private companion object {
        const val VIEW_TYPE_HEADER = 1
        const val VIEW_TYPE_ITEM = 2
    }

    open inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private inner class ItemViewHolder(
        private val binding: DB,
        private val viewModelBinder: (DB, VM) -> Unit,
        viewModelFactory: () -> VM
    ) : ViewHolder(binding.root) {

        lateinit var item: M
        val viewModel: VM = viewModelFactory.invoke()

        init {
            if (onItemClickListener != null) {
                binding.root.setOnClickListener { onItemClickListener.invoke(item) }
            }
        }

        fun bind(item: M) {
            this.item = item
            viewModel.bind(item)
            viewModelBinder.invoke(binding, viewModel)
            binding.executePendingBindings()
        }
    }

    private inner class HeaderViewHolder(view: View) : ViewHolder(view) {
        private val textView = view.findViewById<TextView>(R.id.textView)
        fun bind(headerHolder: HeaderHolder<ID>) {
            textView.text = headerHolder.header
        }
    }

    private class HeaderHolder<ID>(override val id: ID, val header: String) : ModelWithId<ID>

    private class ItemHolder<ID, M : ModelWithId<ID>>(val item: M) : ModelWithId<ID> by item
}