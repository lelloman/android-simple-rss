package com.lelloman.launcher

import com.lelloman.common.view.BaseActivity
import com.lelloman.launcher.databinding.ActivityMainBinding

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override val layoutResId = R.layout.activity_main

    override val hasBaseLayout = false

    override fun getViewModelClass() = MainViewModel::class.java
}
