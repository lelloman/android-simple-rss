package com.lelloman.launcher.classification

import android.support.annotation.VisibleForTesting
import com.lelloman.common.utils.model.DayTime
import com.lelloman.common.utils.model.WeekTime

class TimeEncoder {
    private val weekTimeEncodedWidth = 7
    private val unitSpan = 1.0 / weekTimeEncodedWidth
    private val boundaries = Array(weekTimeEncodedWidth) {
        it * unitSpan to it * unitSpan + unitSpan
    }

    fun encodeWeekTime(time: WeekTime): DoubleArray {
        val scalar = makeScalarWeekTime(time)
        val percent = scalar / WEEK_SCALAR_FACTOR

        return makeTimeDoubleArray(percent)
    }

    @VisibleForTesting
    fun makeTimeDoubleArray(percent: Double): DoubleArray {
        val scaledPercent = (percent * weekTimeEncodedWidth)
        val cursor = scaledPercent.toInt()
        val cursorBoundary = boundaries[cursor]

        val originalPercentInCursor = .5 + (percent - cursorBoundary.first) / unitSpan
        val isAdjacentRight = originalPercentInCursor > 1.0
        val percentInCursor = if (isAdjacentRight) 2.0 - originalPercentInCursor else originalPercentInCursor

        val adjacent = if (isAdjacentRight) {
            var next = cursor + 1
            if (next >= weekTimeEncodedWidth) next = 0
            next
        } else {
            var prev = cursor - 1
            if (prev < 0) prev = weekTimeEncodedWidth - 1
            prev
        }
        val adjacentPercent = 1 - percentInCursor

        return DoubleArray(weekTimeEncodedWidth) {
            when (it) {
                cursor -> percentInCursor
                adjacent -> adjacentPercent
                else -> 0.0
            }
        }
    }

    fun encodeDayTime(dayTime: DayTime): DoubleArray {
        val dayTimePercent = (dayTime.hour * 60 + dayTime.minute) / DAY_SCALAR_FACTOR
        var a = dayTimePercent * 2
        val growing = a > 1.0
        if (growing) a = 2.0 - a
        val b = dayTimePercent
        return doubleArrayOf(a, b)
    }

    @VisibleForTesting
    fun makeScalarWeekTime(time: WeekTime): Int = (time.dayOfWeek - 1) * 24 + time.hourOfDay

    private companion object {
        const val DAY_SCALAR_FACTOR = 60 * 24.0
        const val WEEK_SCALAR_FACTOR = 24 * 7.0
    }
}