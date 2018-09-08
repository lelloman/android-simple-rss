package com.lelloman.read.mock

import com.lelloman.read.core.logger.Logger
import com.lelloman.read.core.logger.LoggerFactory

class MockLoggerFactory(private val logger: Logger = MockLogger()) : LoggerFactory {

    override fun getLogger(tag: String) = logger

}