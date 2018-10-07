package com.lelloman.common.testutils

import com.lelloman.common.logger.Logger
import org.assertj.core.api.Assertions.assertThat

class MockLogger : Logger {

    private var loggedError = false
    private var loggedErrors = mutableListOf<Pair<String, Throwable?>>()

    override fun i(msg: String) = Unit

    override fun d(msg: String) = Unit

    override fun w(msg: String, throwable: Throwable?) = Unit

    override fun e(msg: String, throwable: Throwable?) {
        loggedError = true
        loggedErrors.add(msg to throwable)
    }

    fun assertNeverLoggedError() {
        assertThat(loggedError).isFalse()
    }

    fun assertLoggedError(msg: String, error: Throwable) {
        assertThat(loggedErrors).contains(msg to error)
    }
}