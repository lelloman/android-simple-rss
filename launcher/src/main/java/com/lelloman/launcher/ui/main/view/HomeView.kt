package com.lelloman.launcher.ui.main.view

import android.arch.lifecycle.Observer
import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.lelloman.common.utils.layoutInflater
import com.lelloman.launcher.R
import com.lelloman.launcher.ui.main.HomePage

class HomeView(context: Context, attrs: AttributeSet)
    : ViewPager(context, attrs), Observer<List<HomePage>> {

    private var pages: List<HomePage> = emptyList()

    private val adapter = object : PagerAdapter() {
        override fun isViewFromObject(view: View, theObject: Any) = view == theObject

        override fun getCount() = pages.size

        override fun instantiateItem(container: ViewGroup, position: Int) = pages[position]
            .let { page ->
                val pageView = context.layoutInflater.inflate(
                    R.layout.pager_item_home,
                    container,
                    false
                )
                container.addView(pageView)
                pageView.findViewById<TextView>(R.id.text_view).text = position.toString()
                pageView
            }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }

    override fun onChanged(t: List<HomePage>?) {
        t?.let {
            pages = t
            adapter.notifyDataSetChanged()
        }
    }

    init {
        setAdapter(adapter)
    }
}