package com.lelloman.read.ui.walkthrough.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.lelloman.read.R
import com.lelloman.read.core.view.AnimationViewActionEvent
import com.lelloman.read.core.view.BaseActivity
import com.lelloman.read.databinding.ActivityWalkthroughBinding
import com.lelloman.read.ui.walkthrough.viewmodel.DiscoverUrlSelectedAnimationEvent
import com.lelloman.read.ui.walkthrough.viewmodel.WalkthroughViewModel
import dagger.android.AndroidInjection

class WalkthroughActivity : BaseActivity<WalkthroughViewModel, ActivityWalkthroughBinding>() {

    private lateinit var viewPagerAdapter: WalkthroughPagerAdapter

    override fun getLayoutId() = R.layout.activity_walkthrough

    override fun getViewModelClass() = WalkthroughViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)

        viewPagerAdapter = WalkthroughPagerAdapter(
            context = this,
            lifecycleOwner = this,
            walkthroughViewModel = viewModel
        )
        binding.viewModel = viewModel
        binding.viewPager.offscreenPageLimit = 20
        binding.viewPager.adapter = viewPagerAdapter
        binding.pagerIndicator.viewPager = binding.viewPager
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewPagerAdapter.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        viewPagerAdapter.onRestoreInstanceState(savedInstanceState)
    }

    override fun onAnimationViewActionEvent(animationViewActionEvent: AnimationViewActionEvent) {
        when (animationViewActionEvent) {
            is DiscoverUrlSelectedAnimationEvent -> viewPagerAdapter.onDiscoverUrlSelectedAnimationEvent()
        }
    }

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, WalkthroughActivity::class.java))
        }
    }
}