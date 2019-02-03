package com.lelloman.simplerss.ui.launcher.view

import com.lelloman.common.view.BaseActivity
import com.lelloman.simplerss.R
import com.lelloman.simplerss.databinding.ActivityLauncherBinding

class LauncherActivity : BaseActivity<com.lelloman.simplerss.ui.launcher.viewmodel.LauncherViewModel, ActivityLauncherBinding>() {

    override val layoutResId = R.layout.activity_launcher

    override fun getViewModelClass() = com.lelloman.simplerss.ui.launcher.viewmodel.LauncherViewModel::class.java

    override fun setViewModel(binding: ActivityLauncherBinding, viewModel: com.lelloman.simplerss.ui.launcher.viewmodel.LauncherViewModel) {
        binding.viewModel = viewModel
    }
}