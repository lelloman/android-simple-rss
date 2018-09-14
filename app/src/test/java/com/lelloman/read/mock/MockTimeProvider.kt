package com.lelloman.read.mock

import com.lelloman.common.utils.TimeProvider

class MockTimeProvider(var now: Long = 0) : TimeProvider {
    override fun nowUtcMs() = now
}