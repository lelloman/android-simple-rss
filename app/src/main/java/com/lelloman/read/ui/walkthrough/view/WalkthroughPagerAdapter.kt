package com.lelloman.read.ui.walkthrough.view

import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lelloman.read.R

class WalkthroughPagerAdapter : PagerAdapter() {

    private val viewCache = mutableMapOf<Int, View>()

    override fun isViewFromObject(view: View, obj: Any) = view == obj

    override fun getCount() = LAYOUT_IDS.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutId = LAYOUT_IDS[position]

        val view = if (viewCache.containsKey(layoutId)) {
            viewCache[layoutId]!!
        } else {
            LayoutInflater.from(container.context).inflate(layoutId, container, false)
        }
        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View?)
    }

    private companion object {
        val LAYOUT_IDS = arrayOf(
            R.layout.pager_item_walkthrough_1,
            R.layout.pager_item_walkthrough_2,
            R.layout.pager_item_walkthrough_3
        )
    }
}