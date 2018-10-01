package com.lelloman.deviceinfo.ui.view

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.ViewGroup
import com.lelloman.common.view.BaseMultiTypeRecyclerViewAdapter
import com.lelloman.common.view.ItemType
import com.lelloman.common.view.ResourceProvider
import com.lelloman.common.viewmodel.BaseListItemViewModel
import com.lelloman.deviceinfo.R
import com.lelloman.deviceinfo.databinding.ListItemAudioInfoItemBinding
import com.lelloman.deviceinfo.databinding.ListItemDisplayInfoItemBinding
import com.lelloman.deviceinfo.databinding.ListItemNetworkInfoItemBinding
import com.lelloman.deviceinfo.infoitem.AudioInfoItem
import com.lelloman.deviceinfo.infoitem.DisplayInfoItem
import com.lelloman.deviceinfo.infoitem.InfoItem
import com.lelloman.deviceinfo.infoitem.NetworkInfoItem
import com.lelloman.deviceinfo.ui.viewmodel.AudioInfoItemListItemViewModel
import com.lelloman.deviceinfo.ui.viewmodel.DisplayInfoItemListItemViewModel
import com.lelloman.deviceinfo.ui.viewmodel.NetworkInfoItemListItemViewModel

class InfoAdapter(
    resourceProvider: ResourceProvider,
    onItemClickListener: (Any) -> Unit
) : BaseMultiTypeRecyclerViewAdapter<InfoItem>(
    resourceProvider = resourceProvider,
    onClickListener = onItemClickListener
) {

    @Suppress("UNCHECKED_CAST")
    override val itemsMap: Map<Any, ItemType<InfoItem, BaseListItemViewModel<InfoItem>, ViewDataBinding>>
        get() {
            val out = mutableMapOf<Any, ItemType<InfoItem, BaseListItemViewModel<InfoItem>, ViewDataBinding>>()
            out[DisplayInfoItem::class.java] = Display as ItemType<InfoItem, BaseListItemViewModel<InfoItem>, ViewDataBinding>
            out[NetworkInfoItem::class.java] = Network as ItemType<InfoItem, BaseListItemViewModel<InfoItem>, ViewDataBinding>
            out[AudioInfoItem::class.java] = Audio as ItemType<InfoItem, BaseListItemViewModel<InfoItem>, ViewDataBinding>
            return out
        }
}

private object Display : ItemType<DisplayInfoItem, DisplayInfoItemListItemViewModel, ListItemDisplayInfoItemBinding> {
    override val ordinal = 1
    override fun createBinding(parent: ViewGroup): ViewDataBinding =
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_display_info_item,
            parent,
            false
        )

    override fun createViewModel(resourceProvider: ResourceProvider, onClickListener: ((Any) -> Unit)?): DisplayInfoItemListItemViewModel {
        return DisplayInfoItemListItemViewModel(
            resourceProvider = resourceProvider,
            onDisplayButtonClickListener = onClickListener
        )
    }

    override fun bindViewModel(viewModel: DisplayInfoItemListItemViewModel, binding: ListItemDisplayInfoItemBinding, item: DisplayInfoItem) {
        binding.viewModel = viewModel
    }
}

private object Network : ItemType<NetworkInfoItem, NetworkInfoItemListItemViewModel, ListItemNetworkInfoItemBinding> {
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

    override fun createViewModel(resourceProvider: ResourceProvider, onClickListener: ((Any) -> Unit)?): NetworkInfoItemListItemViewModel =
        NetworkInfoItemListItemViewModel()

    override fun bindViewModel(viewModel: NetworkInfoItemListItemViewModel, binding: ListItemNetworkInfoItemBinding, item: NetworkInfoItem) {
        binding.viewModel = viewModel
        val adapter = NetworkInterfaceAdapter()
        binding.recyclerViewNetworkInfo.adapter = adapter
        adapter.onChanged(item.networkInterfaces)
    }
}

private object Audio : ItemType<AudioInfoItem, AudioInfoItemListItemViewModel, ListItemAudioInfoItemBinding> {
    override val ordinal = 3

    override fun createBinding(parent: ViewGroup): ViewDataBinding = DataBindingUtil.inflate(
        LayoutInflater.from(parent.context),
        R.layout.list_item_audio_info_item,
        parent,
        false
    )

    override fun createViewModel(resourceProvider: ResourceProvider, onClickListener: ((Any) -> Unit)?) = AudioInfoItemListItemViewModel(resourceProvider)

    override fun bindViewModel(viewModel: AudioInfoItemListItemViewModel, binding: ListItemAudioInfoItemBinding, item: AudioInfoItem) {
        binding.viewModel = viewModel
    }
}
