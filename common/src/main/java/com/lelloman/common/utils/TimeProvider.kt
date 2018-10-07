package com.lelloman.common.utils

import com.lelloman.common.utils.model.WeekTime

interface TimeProvider {

    fun nowUtcMs(): Long

    fun getParsedTime(timeMs: Long): WeekTime
}