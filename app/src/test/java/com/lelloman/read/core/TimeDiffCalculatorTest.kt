package com.lelloman.read.core

import com.lelloman.read.testutils.MockResourceProvider
import com.nhaarman.mockito_kotlin.mock

class TimeDiffCalculatorTest {

    private var now = 0L

    private val timeProvider: TimeProvider = mock {
        on { nowUtcMs() }.thenAnswer { now }
    }

    private val tested = TimeDiffCalculator(
        timeProvider = timeProvider,
        resourceProvider = MockResourceProvider()
    )
}