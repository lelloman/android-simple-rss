package com.lelloman.common.utils.model

data class DayTime(
    val hour: Int,
    val minute: Int
) {
    init {
        if (hour < 0 || hour > 23) {
            throw IllegalArgumentException("Argument hour must be in range 0-23, got $hour instead.")
        }

        if (minute < 0 || minute > 59) {
            throw IllegalArgumentException("Argument minute must be in range 0-59, got $minute instead.")
        }
    }
}