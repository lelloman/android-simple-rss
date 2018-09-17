package com.lelloman.deviceinfo

import android.os.Bundle
import com.lelloman.common.view.BaseActivity
import com.lelloman.deviceinfo.databinding.ActivityInfoListBinding

class InfoListActivity : BaseActivity<InfoListViewModel, ActivityInfoListBinding>() {

    override val layoutResId = R.layout.activity_info_list

    override fun getViewModelClass() = InfoListViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
    }
}
