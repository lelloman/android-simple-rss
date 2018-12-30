package com.lelloman.common.view.adapter

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.lelloman.common.utils.ModelWithIdListDiffCalculator
import com.lelloman.common.utils.model.ModelWithId
import com.lelloman.common.viewmodel.BaseListItemViewModel

abstract class BaseRecyclerViewAdapter<ID, M : ModelWithId<ID>, VM : BaseListItemViewModel<ID, M>, DB : ViewDataBinding>(
    private val onItemClickListener: (M) -> Unit = {}
) : RecyclerView.Adapter<BaseRecyclerViewAdapter.BaseViewHolder<ID, M, VM, DB>>(), Observer<List<M>> {

    private var data = emptyList<M>()
    private val listDiffCalculator = ModelWithIdListDiffCalculator<ID>()

    abstract val listItemLayoutResId: Int

    abstract fun bindViewModel(binding: DB, viewModel: VM)

    protected abstract fun createViewModel(viewHolder: BaseViewHolder<ID, M, VM, DB>): VM

    fun getItem(position: Int) = data[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ID, M, VM, DB> {
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

    override fun onBindViewHolder(holder: BaseViewHolder<ID, M, VM, DB>, position: Int) = holder.bind(data[position])

    override fun onChanged(newData: List<M>?) {
        newData?.let {
            val diff = listDiffCalculator.computeDiff(data, newData)
            this.data = newData
            diff.dispatchUpdatesTo(this)
        }
    }

    class BaseViewHolder<ID, M : ModelWithId<ID>, VM : BaseListItemViewModel<ID, M>, DB : ViewDataBinding>(
        private val binding: DB,
        private val viewModelBinder: (DB, VM) -> Unit,
        viewModelFactory: (BaseViewHolder<ID, M, VM, DB>) -> VM,
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