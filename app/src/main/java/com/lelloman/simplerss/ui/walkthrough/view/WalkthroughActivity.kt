package com.lelloman.simplerss.ui.walkthrough.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.lelloman.common.viewmodel.command.SwipePageCommand
import com.lelloman.simplerss.R
import com.lelloman.simplerss.databinding.ActivityWalkthroughBinding
import com.lelloman.simplerss.ui.SimpleRssActivity
import com.lelloman.simplerss.ui.walkthrough.viewmodel.WalkthroughViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class WalkthroughActivity : SimpleRssActivity<WalkthroughViewModel, ActivityWalkthroughBinding>() {

    private lateinit var viewPagerAdapter: WalkthroughPagerAdapter

    override val hasActionBar = false

    override val hasInverseTheme = true

    override val layoutResId = R.layout.activity_walkthrough

    override val viewModel by viewModel<WalkthroughViewModel>()

    override fun setViewModel(
        binding: ActivityWalkthroughBinding,
        viewModel: WalkthroughViewModel
    ) {
        binding.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewPagerAdapter = WalkthroughPagerAdapter(
            context = this,
            lifecycleOwner = this,
            walkthroughViewModel = viewModel
        )
        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(pageIndex: Int) = viewModel.onPageSelected(pageIndex)
        })

        binding.viewPager.offscreenPageLimit = 20
        binding.viewPager.adapter = viewPagerAdapter
        binding.pagerIndicator.viewPager = binding.viewPager
    }

    override fun onSwipePageCommand(swipePageCommand: SwipePageCommand) {
        when (swipePageCommand.direction) {
            SwipePageCommand.Direction.LEFT -> binding.viewPager.currentItem =
                binding.viewPager.currentItem - 1
            SwipePageCommand.Direction.RIGHT -> binding.viewPager.currentItem =
                binding.viewPager.currentItem + 1
        }
    }

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, WalkthroughActivity::class.java))
        }
    }
}