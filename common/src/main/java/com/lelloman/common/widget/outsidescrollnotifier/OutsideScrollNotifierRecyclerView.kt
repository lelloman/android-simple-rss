package com.lelloman.common.widget.outsidescrollnotifier

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.lelloman.common.R

class OutsideScrollNotifierRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr), ViewGroup.OnHierarchyChangeListener {

    private val childrenToNotify = linkedSetOf<OutsideScrollChild>()

    private var notifyChildrenOnLayout = false

    init {
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.OutsideScrollNotifierRecyclerView,
            defStyleAttr,
            0
        )

        try {
            notifyChildrenOnLayout = a.getBoolean(
                R.styleable.OutsideScrollNotifierRecyclerView_notifyChildrenOnLayout,
                notifyChildrenOnLayout
            )
        } finally {
            a.recycle()
        }

        if (notifyChildrenOnLayout) {
            viewTreeObserver.addOnGlobalLayoutListener { notifyChildren() }
        }

        setOnHierarchyChangeListener(this)
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        notifyChildren()
    }

    private fun notifyChildren() = childrenToNotify.forEach {
        val relativeChildTop = (it as View).top
        val childHeight = it.getHeight().toFloat()

        val position: ScrollPosition
        var percent: Float

        if (relativeChildTop < 0) {
            percent = minOf(1f, -relativeChildTop / childHeight)
            position = ScrollPosition.ABOVE
        } else {
            val viewBottom = relativeChildTop + childHeight
            percent = (viewBottom - height) / childHeight
            percent = minOf(maxOf(0f, percent), 1f)

            position = if (percent > 0) ScrollPosition.BELOW else ScrollPosition.INSIDE
        }

        it.onPositionChanged(percent, position)
    }

    override fun onChildViewAdded(parent: View?, child: View?) {
        if (child is ViewGroup) {
            child.setOnHierarchyChangeListener(this)
            for (i in 0 until child.childCount) {
                onChildViewAdded(child, child.getChildAt(i))
            }
        }

        if (child is OutsideScrollChild) {
            childrenToNotify.add(child)
        }
    }

    override fun onChildViewRemoved(parent: View?, child: View?) {
        if (child is ViewGroup) {
            child.setOnHierarchyChangeListener(null)
            for (i in 0 until child.childCount) {
                onChildViewRemoved(child, child.getChildAt(i))
            }
        }

        if (child is OutsideScrollChild && childrenToNotify.contains(child)) {
            childrenToNotify.remove(child)
        }
    }

}