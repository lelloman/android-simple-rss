package com.lelloman.common.widget.bottomdrawer

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout

class BottomDrawerLayout(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private lateinit var bottomDrawerFrame: BottomDrawerFrame
    private lateinit var contentContainer: View

    val state: State
        get() = bottomDrawerFrame.getState()

    enum class State {
        CLOSED,
        MINI,
        FULL
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (childCount != 2) {
            throw IllegalStateException("${BottomDrawerLayout::class.java.name} must have 2 direct children, it's been inflated with $childCount instead")
        }

        contentContainer = getChildAt(0)

        val drawerFrameContentView = getChildAt(1)
        removeView(drawerFrameContentView)
        bottomDrawerFrame = BottomDrawerFrame(context)
        addView(bottomDrawerFrame, LayoutParams(MATCH_PARENT, MATCH_PARENT))
        bottomDrawerFrame.addContentView(drawerFrameContentView)
    }

    fun setDrawerFrameVisibility(visible: Boolean) {
        bottomDrawerFrame.setVisible(visible)
    }

    fun setMini() {
        bottomDrawerFrame.setState(State.MINI, true, BottomDrawerFrame.MAX_ANIMATION_TIME)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (bottomDrawerFrame.isWaitingForUpEvent) {
            bottomDrawerFrame.dispatchTouchEvent(ev)
        } else if (ev.y > bottomDrawerFrame.getVerticalOffset()) {
            val handled = bottomDrawerFrame.dispatchTouchEvent(ev)
            if (!handled) {
                contentContainer.dispatchTouchEvent(ev)
            }
        } else {
            contentContainer.dispatchTouchEvent(ev)
        }
        return true
    }
}
