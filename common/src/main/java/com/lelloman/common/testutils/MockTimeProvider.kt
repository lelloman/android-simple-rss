package com.lelloman.common.testutils

import com.lelloman.common.utils.TimeProvider
import com.lelloman.common.utils.model.WeekTime

class MockTimeProvider(var now: Long = 0) : TimeProvider {
    override fun nowUtcMs() = now

    override fun getParsedTime(timeMs: Long) = WeekTime(0, 0)
}