package com.lelloman.read.ui.walkthrough.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.lelloman.common.navigation.DeepLink
import com.lelloman.common.navigation.DeepLinkStartable
import com.lelloman.common.view.BaseActivity
import com.lelloman.common.view.actionevent.SwipePageActionEvent
import com.lelloman.read.R
import com.lelloman.read.databinding.ActivityWalkthroughBinding
import com.lelloman.read.ui.walkthrough.viewmodel.WalkthroughViewModel
import dagger.android.AndroidInjection

class WalkthroughActivity : BaseActivity<WalkthroughViewModel, ActivityWalkthroughBinding>() {

    private lateinit var viewPagerAdapter: WalkthroughPagerAdapter

    override val hasActionBar = false

    override val hasInverseTheme = true

    override val layoutResId = R.layout.activity_walkthrough

    override fun getViewModelClass() = WalkthroughViewModel::class.java

    override fun setViewModel(binding: ActivityWalkthroughBinding, viewModel: WalkthroughViewModel) {
        binding.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)

        viewPagerAdapter = WalkthroughPagerAdapter(
            context = this,
            lifecycleOwner = this,
            walkthroughViewModel = viewModel
        )
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

        var deepLinkStartable = object : DeepLinkStartable {
            override fun start(context: Context, deepLink: DeepLink) {
                val intent = Intent(context, WalkthroughActivity::class.java)
                if (context !is Activity) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            }
        }
            internal set
    }
}