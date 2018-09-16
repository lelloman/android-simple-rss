package com.lelloman.common.testutils

import com.lelloman.common.utils.TimeProvider

class MockTimeProvider(var now: Long = 0) : TimeProvider {
    override fun nowUtcMs() = now
}