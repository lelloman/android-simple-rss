package com.lelloman.read.widget.outsidescrollnotifier

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet

class OutsideScrollCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr), OutsideScrollChild {

    private val originalElevation: Float = cardElevation
    private var lastPosition = ScrollPosition.INSIDE

    override fun onPositionChanged(percentOutside: Float, position: ScrollPosition) {
        if (lastPosition == ScrollPosition.INSIDE && position == ScrollPosition.INSIDE) return
        lastPosition = position

        cardElevation = (1f - percentOutside) * originalElevation
        alpha = 1f - percentOutside * .5f
        val scale = 1f - percentOutside
        scaleX = scale
        scaleY = scale
        pivotY = if (position == ScrollPosition.ABOVE) height.toFloat() else 0f
        pivotX = width / 2f
        invalidate()
    }
}