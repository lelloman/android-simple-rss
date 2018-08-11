package com.lelloman.read.ui.walkthrough.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.lelloman.read.R
import com.lelloman.read.core.view.BaseActivity
import com.lelloman.read.databinding.ActivityWalkthroughBinding
import com.lelloman.read.ui.walkthrough.viewmodel.WalkthroughViewModel
import dagger.android.AndroidInjection

class WalkthroughActivity : BaseActivity<WalkthroughViewModel, ActivityWalkthroughBinding>() {

    private val viewPagerAdapter = WalkthroughPagerAdapter()

    override fun getLayoutId() = R.layout.activity_walkthrough

    override fun getViewModelClass() = WalkthroughViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)

        binding.viewModel = viewModel
        binding.viewPager.adapter = viewPagerAdapter
        binding.pagerIndicator.viewPager = binding.viewPager
    }

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, WalkthroughActivity::class.java))
        }
    }
}