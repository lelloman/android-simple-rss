package com.lelloman.common.widget.outsidescrollnotifier

interface OutsideScrollChild {
    fun getHeight(): Int
    fun onPositionChanged(percentOutside: Float, position: ScrollPosition)
}