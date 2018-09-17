package com.lelloman.deviceinfo.ui

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.lelloman.common.view.BaseActivity
import com.lelloman.common.view.BaseRecyclerViewAdapter
import com.lelloman.deviceinfo.R
import com.lelloman.deviceinfo.databinding.ActivityInfoListBinding
import com.lelloman.deviceinfo.databinding.ListItemInfoItemBinding
import com.lelloman.deviceinfo.infoitem.InfoItem

class InfoListActivity : BaseActivity<InfoListViewModel, ActivityInfoListBinding>() {

    override val layoutResId = R.layout.activity_info_list

    private val adapter = object : BaseRecyclerViewAdapter<InfoItem, InfoItemListItemViewModel, ListItemInfoItemBinding>() {

        override val listItemLayoutResId = R.layout.list_item_info_item

        override fun bindViewModel(binding: ListItemInfoItemBinding, viewModel: InfoItemListItemViewModel) {
            binding.viewModel = viewModel
        }

        override fun createViewModel(viewHolder: BaseViewHolder<InfoItem, InfoItemListItemViewModel, ListItemInfoItemBinding>)
            : InfoItemListItemViewModel = InfoItemListItemViewModel(
            resourceProvider = resourceProvider
        )
    }

    override fun getViewModelClass() = InfoListViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        viewModel.deviceInfos.observe(this, adapter)
    }
}
