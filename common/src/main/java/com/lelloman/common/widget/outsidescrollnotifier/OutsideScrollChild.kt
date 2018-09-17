package com.lelloman.common.widget.outsidescrollnotifier

interface OutsideScrollChild {
    fun getHeight(): Int
    fun getLocationOnScreen(location: IntArray)
    fun onPositionChanged(percentOutside: Float, position: ScrollPosition)
}