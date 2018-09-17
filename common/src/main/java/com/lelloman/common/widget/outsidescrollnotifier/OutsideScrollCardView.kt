package com.lelloman.common.widget.outsidescrollnotifier

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator

class OutsideScrollCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr), OutsideScrollChild {

    private val originalElevation: Float = cardElevation
    private var lastPosition = ScrollPosition.INSIDE

    private val accInterpolator = AccelerateInterpolator(1.8f)
    private val decInterpolator = DecelerateInterpolator(1.2f)
    
    override fun onPositionChanged(percentOutside: Float, position: ScrollPosition) {
        if (lastPosition == ScrollPosition.INSIDE && position == ScrollPosition.INSIDE) return
        lastPosition = position
        
        val accInterpolatedPercent = accInterpolator.getInterpolation(percentOutside)
        val decInterpolatedPercent = decInterpolator.getInterpolation(percentOutside)
        
        cardElevation = (1f - decInterpolatedPercent) * originalElevation
        alpha = 1f - accInterpolatedPercent * .5f
        val scale = 1f - accInterpolatedPercent * .2f
        scaleX = scale
        scaleY = scale
        pivotY = if (position == ScrollPosition.ABOVE) height.toFloat() else 0f
        pivotX = width / 2f
        
        invalidate()
    }
}