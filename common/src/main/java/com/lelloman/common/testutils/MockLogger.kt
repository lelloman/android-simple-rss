package com.lelloman.common.testutils

import com.lelloman.common.logger.Logger

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
        if (loggedError) {
            throw AssertionError("An error WAS logged.")
        }
    }

    fun assertLoggedError(msg: String, error: Throwable) {
        if (!loggedErrors.contains(msg to error)) {
            throw AssertionError("The expected msg and error were not logged.")
        }
    }
}