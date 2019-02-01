package com.lelloman.read.testutils

import com.lelloman.common.logger.Logger
import com.lelloman.common.logger.LoggerFactory
import com.nhaarman.mockito_kotlin.mock

class MockLoggerFactory(private val logger: Logger = MockLogger()) : LoggerFactory {

    override fun getLogger(clazz: Class<*>) = logger
}