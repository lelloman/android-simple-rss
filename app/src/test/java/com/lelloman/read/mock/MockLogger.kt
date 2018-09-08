package com.lelloman.read.mock

import com.google.common.truth.Truth.assertThat
import com.lelloman.read.core.logger.Logger

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