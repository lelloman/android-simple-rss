package com.lelloman.read.ui.walkthrough.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.lelloman.read.R
import com.lelloman.read.core.ResourceProvider
import com.lelloman.read.core.view.BaseActivity
import com.lelloman.read.core.view.actionevent.SwipePageActionEvent
import com.lelloman.read.databinding.ActivityWalkthroughBinding
import com.lelloman.read.ui.walkthrough.viewmodel.WalkthroughViewModel
import dagger.android.AndroidInjection
import javax.inject.Inject

class WalkthroughActivity : BaseActivity<WalkthroughViewModel, ActivityWalkthroughBinding>() {

    private lateinit var viewPagerAdapter: WalkthroughPagerAdapter

    @Inject
    lateinit var resourceProvider: ResourceProvider

    override val hasActionBar = false

    override val hasInverseTheme = true

    override val layoutResId = R.layout.activity_walkthrough

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

    override fun onSwipePageActionEvent(swipePageActionEvent: SwipePageActionEvent) {
        when (swipePageActionEvent.direction) {
            SwipePageActionEvent.Direction.LEFT -> binding.viewPager.currentItem = binding.viewPager.currentItem - 1
            SwipePageActionEvent.Direction.RIGHT -> binding.viewPager.currentItem = binding.viewPager.currentItem + 1
        }
    }

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, WalkthroughActivity::class.java))
        }
    }
}