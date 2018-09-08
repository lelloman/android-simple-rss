package com.lelloman.read.mock

import com.lelloman.read.core.TimeProvider

class MockTimeProvider(var now: Long = 0) : TimeProvider {
    override fun nowUtcMs() = now
}