package com.lelloman.launcher.ui.launches.view

import android.os.Bundle
import com.lelloman.common.view.BaseActivity
import com.lelloman.launcher.databinding.ActivityLaunchesBinding
import com.lelloman.launcher.ui.launches.viewmodel.LaunchesViewModel
import com.lelloman.launcher.R

class LaunchesActivity : BaseActivity<LaunchesViewModel, ActivityLaunchesBinding>() {

    override val layoutResId = R.layout.activity_launches

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun setViewModel(binding: ActivityLaunchesBinding, viewModel: LaunchesViewModel) {
        binding.viewModel = viewModel
    }

    override fun getViewModelClass() = LaunchesViewModel::class.java

}