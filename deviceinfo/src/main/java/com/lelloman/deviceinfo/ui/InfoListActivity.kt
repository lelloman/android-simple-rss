package com.lelloman.deviceinfo.ui

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.lelloman.common.view.BaseActivity
import com.lelloman.deviceinfo.R
import com.lelloman.deviceinfo.databinding.ActivityInfoListBinding

class InfoListActivity : BaseActivity<InfoListViewModel, ActivityInfoListBinding>() {

    override val layoutResId = R.layout.activity_info_list

    private val adapter by lazy { InfoAdapter(resourceProvider) }

    override fun getViewModelClass() = InfoListViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        viewModel.deviceInfos.observe(this, adapter)
    }
}
