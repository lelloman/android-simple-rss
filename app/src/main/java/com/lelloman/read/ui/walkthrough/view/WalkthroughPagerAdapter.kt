package com.lelloman.read.ui.walkthrough.view

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.constraint.ConstraintSet
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.LinearLayoutManager
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lelloman.read.R
import com.lelloman.read.databinding.PagerItemWalkthroughDiscoverBinding
import com.lelloman.read.ui.walkthrough.viewmodel.WalkthroughViewModel

class WalkthroughPagerAdapter(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val walkthroughViewModel: WalkthroughViewModel
) : PagerAdapter() {

    private lateinit var discoverBinding: PagerItemWalkthroughDiscoverBinding

    private var discoverScene2Animated = false

    private val discoverScene2ConstraintSet by lazy {
        ConstraintSet().apply { clone(context, R.layout.pager_item_walkthrough_discover_set2) }
    }

    override fun isViewFromObject(view: View, obj: Any) = view == obj

    override fun getCount() = LAYOUT_IDS.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutId = LAYOUT_IDS[position]

        val view = LayoutInflater.from(container.context).inflate(layoutId, container, false)

        when (layoutId) {
            R.layout.pager_item_walkthrough_discover -> {
                discoverBinding = DataBindingUtil.bind(view)!!
                discoverBinding.viewModel = walkthroughViewModel
                val foundFeedsAdapter = FoundFeedsAdapter()
                walkthroughViewModel.foundFeeds.observe(lifecycleOwner, foundFeedsAdapter)
                discoverBinding.discoverRecyclerView.adapter = foundFeedsAdapter
                discoverBinding.discoverRecyclerView.layoutManager = LinearLayoutManager(context)
                discoverBinding.setLifecycleOwner(lifecycleOwner)
                if (discoverScene2Animated) {
                    discoverScene2ConstraintSet.applyTo(discoverBinding.constraintLayout)
                }
            }
        }
        container.addView(view)

        return view
    }

    fun onDiscoverUrlSelectedAnimationEvent() {
        if (!discoverScene2Animated) {
            discoverScene2Animated = true

            val constraint1 = ConstraintSet()
            constraint1.clone(discoverBinding.constraintLayout)

            TransitionManager.beginDelayedTransition(discoverBinding.constraintLayout)
            discoverScene2ConstraintSet.applyTo(discoverBinding.constraintLayout)
        }
    }

    fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(KEY_DISCOVER_SCENE_2_ANIMATED, discoverScene2Animated)
    }

    fun onRestoreInstanceState(savedState: Bundle) {
        discoverScene2Animated = savedState.getBoolean(KEY_DISCOVER_SCENE_2_ANIMATED, false)
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View?)
    }

    private companion object {
        val LAYOUT_IDS = arrayOf(
            R.layout.pager_item_walkthrough_discover,
            R.layout.pager_item_walkthrough_2,
            R.layout.pager_item_walkthrough_3
        )

        const val KEY_DISCOVER_SCENE_2_ANIMATED = "DiscoverScene2Animated"
    }
}