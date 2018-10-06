package com.lelloman.launcher.ui.launches.view

import com.lelloman.common.view.BaseActivity
import com.lelloman.launcher.R
import com.lelloman.launcher.databinding.ActivityLaunchesBinding
import com.lelloman.launcher.ui.launches.viewmodel.LaunchesViewModel

class LaunchesActivity : BaseActivity<LaunchesViewModel, ActivityLaunchesBinding>() {

    override val layoutResId = R.layout.activity_launches

    override fun setViewModel(binding: ActivityLaunchesBinding, viewModel: LaunchesViewModel) {
        binding.viewModel = viewModel
    }

    override fun getViewModelClass() = LaunchesViewModel::class.java

}