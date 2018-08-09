package com.lelloman.read.ui.launcher.view

import android.os.Bundle
import android.os.PersistableBundle
import com.lelloman.read.R
import com.lelloman.read.core.view.BaseActivity
import com.lelloman.read.databinding.ActivityLauncherBinding
import com.lelloman.read.ui.launcher.viewmodel.LauncherViewModel
import dagger.android.AndroidInjection

class LauncherActivity : BaseActivity<LauncherViewModel, ActivityLauncherBinding>() {

    override fun getLayoutId() = R.layout.activity_launcher

    override fun getViewModelClass() = LauncherViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        AndroidInjection.inject(this)

        binding.viewModel = viewModel
    }
}