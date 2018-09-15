package com.lelloman.read.mock

import com.lelloman.common.logger.Logger
import com.lelloman.common.logger.LoggerFactory

class MockLoggerFactory(private val logger: Logger = MockLogger()) : LoggerFactory {

    override fun getLogger(tag: String) = logger

}