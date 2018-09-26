package com.lelloman.deviceinfo.ui

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
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

class InfoAdapter(
    private val resourceProvider: ResourceProvider,
    private val onItemClickListener: (InfoItem) -> Unit
) : RecyclerView.Adapter<InfoAdapter.ViewHolder>(), Observer<List<InfoItem>> {

    private var data = emptyList<InfoItem>()
    private val listItemDiffCalculator = ModelWithIdListDiffCalculator()

    private val infoListItemsMap = mapOf(
        DisplayInfoItem::class.java to InfoListItem.DISPLAY,
        NetworkInfoItem::class.java to InfoListItem.NETWORK
    )

    private val viewTypesMap = infoListItemsMap
        .values
        .map { it.ordinal to it }
        .toMap()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val infoListItem = viewTypesMap[viewType]!!
        val binding = infoListItem.binderCreator.invoke(parent)

        return ViewHolder(
            binding = binding,
            onClickListener = onItemClickListener,
            viewModel = infoListItem.viewModelCreator.invoke(resourceProvider, onItemClickListener)
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
        infoListItem.viewModelBinder.invoke(holder.binding, holder.viewModel)
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

    enum class InfoListItem(
        val viewModelBinder: (ViewDataBinding, InfoItemListItemViewModel<*>) -> Unit,
        val binderCreator: (ViewGroup) -> ViewDataBinding,
        val viewModelCreator: (ResourceProvider, (InfoItem) -> Unit) -> InfoItemListItemViewModel<InfoItem>
    ) {

        DISPLAY(
            viewModelBinder = { db, viewModel ->
                (db as ListItemDisplayInfoItemBinding).let { _ ->
                    (viewModel as DisplayInfoItemListItemViewModel).let { _ ->
                        db.viewModel = viewModel
                    }
                }
            },
            binderCreator = { parent ->
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.list_item_display_info_item,
                    parent,
                    false
                )
            },
            viewModelCreator = { resourceProvider, onClickListener ->
                DisplayInfoItemListItemViewModel(
                    resourceProvider = resourceProvider,
                    onDisplayButtonClickListener = onClickListener
                ) as InfoItemListItemViewModel<InfoItem>
            }),
        NETWORK(
            viewModelBinder = { db, viewModel ->
                (db as ListItemNetworkInfoItemBinding).let { _->
                    (viewModel as NetworkInfoItemListItemViewModel) .let { _ ->
                        db.viewModel = viewModel
                    }
                }
            },
            binderCreator = { parent ->
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.list_item_network_info_item,
                    parent,
                    false
                )
            },
            viewModelCreator = { _, _ ->
                NetworkInfoItemListItemViewModel() as InfoItemListItemViewModel<InfoItem>
            })
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