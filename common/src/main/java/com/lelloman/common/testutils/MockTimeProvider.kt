package com.lelloman.common.testutils

import com.lelloman.common.utils.TimeProvider
import com.lelloman.common.utils.model.DayTime
import com.lelloman.common.utils.model.WeekTime

class MockTimeProvider(var now: Long = 0) : TimeProvider {
    override fun nowUtcMs() = now

    override fun getParsedWeekTime(timeMs: Long) = WeekTime(0, 0)

    override fun getParsedDayTime(timeMs: Long) = DayTime(0, 0)
}