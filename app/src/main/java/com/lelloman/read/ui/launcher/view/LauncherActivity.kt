package com.lelloman.read.ui.launcher.view

import com.lelloman.common.view.BaseActivity
import com.lelloman.read.R
import com.lelloman.read.databinding.ActivityLauncherBinding
import com.lelloman.read.ui.launcher.viewmodel.LauncherViewModel

class LauncherActivity : BaseActivity<LauncherViewModel, ActivityLauncherBinding>() {

    override val layoutResId = R.layout.activity_launcher

    override fun getViewModelClass() = LauncherViewModel::class.java

    override fun setViewModel(binding: ActivityLauncherBinding, viewModel: LauncherViewModel) {
        binding.viewModel = viewModel
    }
}