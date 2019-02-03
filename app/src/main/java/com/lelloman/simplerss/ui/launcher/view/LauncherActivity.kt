package com.lelloman.simplerss.ui.launcher.view

import com.lelloman.common.view.BaseActivity
import com.lelloman.simplerss.R
import com.lelloman.simplerss.databinding.ActivityLauncherBinding
import com.lelloman.simplerss.ui.launcher.viewmodel.LauncherViewModel

class LauncherActivity : BaseActivity<LauncherViewModel, ActivityLauncherBinding>() {

    override val layoutResId = R.layout.activity_launcher

    override fun getViewModelClass() = LauncherViewModel::class.java

    override fun setViewModel(binding: ActivityLauncherBinding, viewModel: LauncherViewModel) {
        binding.viewModel = viewModel
    }
}