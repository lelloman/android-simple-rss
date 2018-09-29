package com.lelloman.launcher.ui

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import com.lelloman.common.widget.bottomdrawer.BottomDrawerFrame
import com.lelloman.common.widget.bottomdrawer.BottomDrawerLayout
import com.lelloman.launcher.R

@SuppressLint("ClickableViewAccessibility")
class AppsDrawerView(
    context: Context,
    attrs: AttributeSet
) : FrameLayout(context, attrs), BottomDrawerFrame.BottomDrawerFrameContent {

    private var miniViewOffset = 0f
    private var miniViewFadeThresholdBottom = 0f
    private var miniViewFadeThresholdTop = 0f
    private var miniViewFadeRange = 0f

    private val miniView by lazy { findViewById<View>(R.id.header) }
    private val fullView by lazy { findViewById<View>(R.id.recycler_view_apps) }

    private val appsRecyclerView by lazy { findViewById<RecyclerView>(R.id.recycler_view_apps) }

    override val headerHeight = context.resources.getDimensionPixelSize(R.dimen.apps_drawer_header_height)

    val recyclerView: RecyclerView by lazy { appsRecyclerView }

    private var overscrolling = false

    init {
        inflate(context, R.layout.view_apps_drawer, this)
        recyclerView.layoutManager = object : GridLayoutManager(
            context,
            5
        ) {
            override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
                val scrollRange = super.scrollVerticallyBy(dy, recycler, state)
                val overscroll = dy - scrollRange
                if (overscroll < 0) {
                    overscrolling = true
                    return 0
                }
                return scrollRange
            }
        }

        recyclerView.setOnTouchListener { _, _ ->
            overscrolling
        }
    }

    override fun onStateChanged(state: BottomDrawerLayout.State) {
        overscrolling = false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_UP) {
            overscrolling = false
        }
        Log.d("ASD", "onIntercept overscroll $overscrolling $ev")
        return overscrolling
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        miniViewOffset = (h - headerHeight).toFloat()
        miniViewFadeThresholdBottom = miniViewOffset * .666f
        miniViewFadeThresholdTop = miniViewOffset * .333f
        miniViewFadeRange = miniViewFadeThresholdBottom - miniViewFadeThresholdTop
    }

    override fun setVerticalOffset(verticalOffset: Float) {
        val miniViewAlpha: Float = when {
            verticalOffset > miniViewFadeThresholdBottom -> 1f
            verticalOffset < miniViewFadeThresholdTop -> 0f
            else -> 1 - (miniViewFadeThresholdBottom - verticalOffset) / miniViewFadeRange
        }
        miniView.alpha = miniViewAlpha

        fullView.visibility = if (verticalOffset >= miniViewOffset) View.GONE else View.VISIBLE
        miniView.visibility = if (verticalOffset == 0f) View.GONE else View.VISIBLE
    }
}