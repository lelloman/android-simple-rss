package com.lelloman.read.core.view

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.lelloman.common.utils.ModelWithId
import com.lelloman.common.utils.ModelWithIdListDiffCalculator
import com.lelloman.read.core.viewmodel.BaseListItemViewModel

abstract class BaseRecyclerViewAdapter<M : ModelWithId, VM : BaseListItemViewModel<M>, DB : ViewDataBinding>(
    private val onItemClickListener: (M) -> Unit = {}
) : RecyclerView.Adapter<BaseRecyclerViewAdapter.BaseViewHolder<M, VM, DB>>(), Observer<List<M>> {

    private var data = emptyList<M>()
    private val listDiffCalculator = ModelWithIdListDiffCalculator()

    abstract val listItemLayoutResId: Int

    abstract fun bindViewModel(binding: DB, viewModel: VM)

    protected abstract fun createViewModel(viewHolder: BaseViewHolder<M, VM, DB>): VM

    fun getItem(position: Int) = data[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<M, VM, DB> {
        val binding: DB = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            listItemLayoutResId,
            parent,
            false
        )

        return BaseViewHolder(
            binding = binding,
            viewModelFactory = ::createViewModel,
            onClickListener = onItemClickListener,
            viewModelBinder = ::bindViewModel
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: BaseViewHolder<M, VM, DB>, position: Int) = holder.bind(data[position])

    override fun onChanged(newData: List<M>?) {
        newData?.let {
            val diff = listDiffCalculator.computeDiff(data, newData)
            this.data = newData
            diff.dispatchUpdatesTo(this)
        }
    }

    class BaseViewHolder<M : ModelWithId, VM : BaseListItemViewModel<M>, DB : ViewDataBinding>(
        private val binding: DB,
        private val viewModelBinder: (DB, VM) -> Unit,
        viewModelFactory: (BaseViewHolder<M, VM, DB>) -> VM,
        onClickListener: ((M) -> Unit)? = null
    ) : RecyclerView.ViewHolder(binding.root) {

        lateinit var item: M
        val viewModel: VM = viewModelFactory.invoke(this)

        init {
            if (onClickListener != null) {
                binding.root.setOnClickListener { onClickListener.invoke(item) }
            }
        }

        fun bind(item: M) {
            this.item = item
            viewModel.bind(item)
            viewModelBinder.invoke(binding, viewModel)
            binding.executePendingBindings()
        }
    }
}