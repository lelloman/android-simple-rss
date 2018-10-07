package com.lelloman.common.utils.model

data class WeekTime
@Throws(IllegalArgumentException::class) constructor(
    val dayOfWeek: Int,
    val hourOfDay: Int
) {

    init {
        if (dayOfWeek < SUNDAY || dayOfWeek > SATURDAY) {
            throw IllegalArgumentException("Argument dayOfWeek must be in range $SUNDAY-$SATURDAY, got $dayOfWeek instead.")
        }

        if (hourOfDay < 0 || hourOfDay > 23) {
            throw IllegalArgumentException("Argument hourOfDay must be in range 0-23, got $hourOfDay instead.")
        }
    }

    companion object {
        const val SUNDAY = 1
        const val MONDAY = 2
        const val TUESDAY = 3
        const val WEDNESDAY = 4
        const val THURSDAY = 5
        const val FRIDAY = 6
        const val SATURDAY = 7
    }
}