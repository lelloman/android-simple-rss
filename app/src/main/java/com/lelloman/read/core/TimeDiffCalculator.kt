package com.lelloman.read.core

import com.lelloman.read.R

class TimeDiffCalculator(
    private val timeProvider: TimeProvider,
    private val resourceProvider: ResourceProvider
) {

    private val minutesThreshold = (60 * 60) - 1
    private val hoursThreshold = (60 * 60 * 24) - 1

    fun getTimeDiffString(timeUtcMs: Long): String {
        var delta = (timeProvider.nowUtcMs() - timeUtcMs) / 1000
        if (delta < 0) {
            delta = 0
        }

        val stringId: Int
        val value: Long

        when {
            delta <= 59 -> {
                stringId = R.string.time_diff_seconds
                value = delta
            }
            delta <= minutesThreshold -> {
                stringId = R.string.time_diff_minutes
                value = delta / 60
            }
            delta <= hoursThreshold -> {
                stringId = R.string.time_diff_hours
                value = delta / 3600
            }
            else -> {
                stringId = R.string.time_diff_days
                value = delta / (3600 * 24)
            }
        }

        return resourceProvider.getString(stringId, value)
    }
}