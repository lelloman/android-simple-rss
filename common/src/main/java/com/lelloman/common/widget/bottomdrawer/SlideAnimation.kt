package com.lelloman.common.widget.bottomdrawer

import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.Transformation

class SlideAnimation(
    duration: Long,
    private val startOffset: Float,
    private val endOffset: Float,
    private val listener: Listener
) : Animation(), Animation.AnimationListener {

    private val deltaOffset: Float = endOffset - startOffset

    init {
        setDuration(duration)
        listener.setAnimating(true)
        setAnimationListener(this)
        interpolator = DecelerateInterpolator()
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
        listener.setVerticalOffset(startOffset + interpolatedTime * deltaOffset)
    }

    override fun onAnimationStart(animation: Animation) {

    }

    override fun onAnimationEnd(animation: Animation) {
        listener.setVerticalOffset(endOffset)
        listener.setAnimating(false)
    }

    override fun onAnimationRepeat(animation: Animation) {

    }

    interface Listener {
        fun setAnimating(animating: Boolean)
        fun setVerticalOffset(verticalOffset: Float)
    }
}
