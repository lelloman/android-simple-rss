package com.lelloman.read.ui.walkthrough.view

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lelloman.read.R
import com.lelloman.read.core.ResourceProvider
import com.lelloman.read.databinding.LayoutDiscoverUrlBinding
import com.lelloman.read.ui.walkthrough.viewmodel.WalkthroughViewModel

class WalkthroughPagerAdapter(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val walkthroughViewModel: WalkthroughViewModel,
    private val resourceProvider: ResourceProvider
) : PagerAdapter() {

    private lateinit var discoverBinding: LayoutDiscoverUrlBinding

    override fun isViewFromObject(view: View, obj: Any) = view == obj

    override fun getCount() = LAYOUT_IDS.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutId = LAYOUT_IDS[position]

        val view = LayoutInflater.from(container.context).inflate(layoutId, container, false)

        when (layoutId) {
            R.layout.layout_discover_url -> {
                discoverBinding = DataBindingUtil.bind(view)!!
                discoverBinding.viewModel = walkthroughViewModel
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
            R.layout.layout_discover_url,
            R.layout.pager_item_walkthrough_2,
            R.layout.pager_item_walkthrough_3
        )
    }
}