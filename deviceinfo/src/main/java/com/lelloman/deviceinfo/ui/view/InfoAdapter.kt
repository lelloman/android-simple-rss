package com.lelloman.deviceinfo.ui.view

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.lelloman.common.utils.ModelWithIdListDiffCalculator
import com.lelloman.common.view.ResourceProvider
import com.lelloman.deviceinfo.R
import com.lelloman.deviceinfo.databinding.ListItemDisplayInfoItemBinding
import com.lelloman.deviceinfo.databinding.ListItemNetworkInfoItemBinding
import com.lelloman.deviceinfo.infoitem.DisplayInfoItem
import com.lelloman.deviceinfo.infoitem.InfoItem
import com.lelloman.deviceinfo.infoitem.NetworkInfoItem
import com.lelloman.deviceinfo.ui.viewmodel.DisplayInfoItemListItemViewModel
import com.lelloman.deviceinfo.ui.viewmodel.InfoItemListItemViewModel
import com.lelloman.deviceinfo.ui.viewmodel.NetworkInfoItemListItemViewModel

class InfoAdapter(
    private val resourceProvider: ResourceProvider,
    private val onItemClickListener: (InfoItem) -> Unit
) : RecyclerView.Adapter<InfoAdapter.ViewHolder>(), Observer<List<InfoItem>> {

    private var data = emptyList<InfoItem>()
    private val listItemDiffCalculator = ModelWithIdListDiffCalculator()

    private val infoListItemsMap = mapOf(
        DisplayInfoItem::class.java to Display,
        NetworkInfoItem::class.java to Network
    )

    private val viewTypesMap = infoListItemsMap
        .values
        .map { it.ordinal to it }
        .toMap()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val infoListItem = viewTypesMap[viewType]!!
        val binding = infoListItem.createBinding(parent)

        return ViewHolder(
            binding = binding,
            onClickListener = onItemClickListener,
            viewModel = infoListItem.createVieModel(resourceProvider, onItemClickListener)
        )
    }

    override fun getItemCount() = data.size

    override fun getItemViewType(position: Int) =
        getInfoListItem(getItem(position)).ordinal

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val infoListItem = getInfoListItem(item)
        holder.bind(item)
        holder.viewModel.bind(item)
        infoListItem.bindViewModel(holder)
        holder.binding.executePendingBindings()
    }

    override fun onChanged(newData: List<InfoItem>?) {
        newData?.let {
            val diff = listItemDiffCalculator.computeDiff(data, newData)
            this.data = newData
            diff.dispatchUpdatesTo(this)
        }
    }

    private fun getItem(position: Int): InfoItem = data[position]

    private fun getInfoListItem(item: Any): InfoListItem {
        return infoListItemsMap[item::class.java]!!
    }


    class ViewHolder(
        val binding: ViewDataBinding,
        onClickListener: ((InfoItem) -> Unit)? = null,
        val viewModel: InfoItemListItemViewModel<InfoItem>
    ) : RecyclerView.ViewHolder(binding.root) {

        lateinit var item: InfoItem

        init {
            if (onClickListener != null) {
                binding.root.setOnClickListener { onClickListener.invoke(item) }
            }
        }

        fun bind(item: InfoItem) {
            this.item = item
        }
    }
}

sealed class InfoListItem {
    abstract val ordinal: Int
    abstract fun bindViewModel(viewHolder: InfoAdapter.ViewHolder)
    abstract fun createBinding(parent: ViewGroup): ViewDataBinding
    abstract fun createVieModel(resourceProvider: ResourceProvider, onClickListener: (InfoItem) -> Unit): InfoItemListItemViewModel<InfoItem>
}

object Display : InfoListItem() {
    override val ordinal = 1
    override fun createBinding(parent: ViewGroup): ViewDataBinding =
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_display_info_item,
            parent,
            false
        )

    override fun createVieModel(resourceProvider: ResourceProvider, onClickListener: (InfoItem) -> Unit): InfoItemListItemViewModel<InfoItem> {
        return DisplayInfoItemListItemViewModel(
            resourceProvider = resourceProvider,
            onDisplayButtonClickListener = onClickListener
        ) as InfoItemListItemViewModel<InfoItem>
    }

    override fun bindViewModel(viewHolder: InfoAdapter.ViewHolder) =
        (viewHolder.binding as ListItemDisplayInfoItemBinding).let { binding ->
            (viewHolder.viewModel as DisplayInfoItemListItemViewModel).let { viewModel ->
                binding.viewModel = viewModel
            }
        }
}

object Network : InfoListItem() {
    override val ordinal = 2
    override fun createBinding(parent: ViewGroup): ViewDataBinding {
        val binding: ListItemNetworkInfoItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_network_info_item,
            parent,
            false
        )
        binding.recyclerViewNetworkInfo.layoutManager = LinearLayoutManager(parent.context)
        return binding
    }

    override fun createVieModel(resourceProvider: ResourceProvider, clickListener: (InfoItem) -> Unit): InfoItemListItemViewModel<InfoItem> {
        return NetworkInfoItemListItemViewModel() as InfoItemListItemViewModel<InfoItem>
    }

    override fun bindViewModel(viewHolder: InfoAdapter.ViewHolder) =
        (viewHolder.binding as ListItemNetworkInfoItemBinding).let { binding ->
            (viewHolder.viewModel as NetworkInfoItemListItemViewModel).let { viewModel ->
                binding.viewModel = viewModel
                val adapter = NetworkInterfaceAdapter()
                binding.recyclerViewNetworkInfo.adapter = adapter
                adapter.onChanged((viewHolder.item as NetworkInfoItem).networkInterfaces)
            }
        }
}
