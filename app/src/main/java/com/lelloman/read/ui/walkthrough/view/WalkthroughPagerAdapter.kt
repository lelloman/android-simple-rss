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
import com.lelloman.read.core.ResourceProvider
import com.lelloman.read.databinding.PagerItemWalkthroughDiscoverBinding
import com.lelloman.read.feed.finder.FoundFeed
import com.lelloman.read.ui.walkthrough.viewmodel.WalkthroughViewModel

class WalkthroughPagerAdapter(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val walkthroughViewModel: WalkthroughViewModel,
    private val resourceProvider: ResourceProvider
) : PagerAdapter() {

    private lateinit var discoverBinding: PagerItemWalkthroughDiscoverBinding

    override fun isViewFromObject(view: View, obj: Any) = view == obj

    override fun getCount() = LAYOUT_IDS.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutId = LAYOUT_IDS[position]

        val view = LayoutInflater.from(container.context).inflate(layoutId, container, false)

        when (layoutId) {
            R.layout.pager_item_walkthrough_discover -> {
                discoverBinding = DataBindingUtil.bind(view)!!
                discoverBinding.viewModel = walkthroughViewModel
//                val foundFeedsAdapter = FoundFeedsAdapter(
//                    resourceProvider = resourceProvider,
//                    onFoundFeedClickListener = onFoundFeedClickListener
//                )
//                walkthroughViewModel.foundFeeds.observe(lifecycleOwner, foundFeedsAdapter)
//                discoverBinding.discoverRecyclerView.adapter = foundFeedsAdapter
//                discoverBinding.discoverRecyclerView.layoutManager = LinearLayoutManager(context)
                discoverBinding.setLifecycleOwner(lifecycleOwner)
            }
        }
        container.addView(view)

        return view
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
    }
}