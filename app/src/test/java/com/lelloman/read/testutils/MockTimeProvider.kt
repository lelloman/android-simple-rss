package com.lelloman.read.testutils

import com.lelloman.common.utils.TimeProvider
import com.lelloman.common.utils.model.DayTime
import com.lelloman.common.utils.model.Time
import com.lelloman.common.utils.model.WeekTime

class MockTimeProvider(var now: Long = 0) : TimeProvider {
    override fun nowUtcMs() = now

    override fun now()= Time(WeekTime(0,0), DayTime(0, 0))

    override fun getTime(utcMs: Long) = now()
}