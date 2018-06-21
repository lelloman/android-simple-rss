package com.lelloman.read.core

import com.lelloman.read.testutils.MockResourceProvider
import com.nhaarman.mockito_kotlin.mock

class SemanticTimeProviderTest {

    private var now = 0L

    private val timeProvider: TimeProvider = mock {
        on { nowUtcMs() }.thenAnswer { now }
    }

    private val tested = SemanticTimeProvider(
        timeProvider = timeProvider,
        resourceProvider = MockResourceProvider()
    )
}