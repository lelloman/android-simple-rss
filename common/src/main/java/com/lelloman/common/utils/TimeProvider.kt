package com.lelloman.common.utils

import com.lelloman.common.utils.model.DayTime
import com.lelloman.common.utils.model.WeekTime

interface TimeProvider {

    fun nowUtcMs(): Long

    fun getParsedWeekTime(timeMs: Long): WeekTime

    fun getParsedDayTime(timeMs: Long): DayTime
}