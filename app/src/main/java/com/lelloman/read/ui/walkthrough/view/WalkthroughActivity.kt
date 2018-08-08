package com.lelloman.read.ui.walkthrough.view

import android.os.Bundle
import android.os.PersistableBundle
import com.lelloman.read.R
import com.lelloman.read.core.view.BaseActivity
import com.lelloman.read.databinding.ActivityWalkthroughBinding
import com.lelloman.read.ui.walkthrough.viewmodel.WalkthroughViewModel
import dagger.android.AndroidInjection

class WalkthroughActivity : BaseActivity<WalkthroughViewModel, ActivityWalkthroughBinding>() {

    override fun getLayoutId() = R.layout.activity_walkthrough

    override fun getViewModelClass() = WalkthroughViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        AndroidInjection.inject(this)

        binding.viewModel = viewModel
    }
}