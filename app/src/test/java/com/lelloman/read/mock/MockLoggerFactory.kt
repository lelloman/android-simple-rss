package com.lelloman.read.mock

import com.lelloman.read.core.logger.Logger
import com.lelloman.read.core.logger.LoggerFactory
import com.nhaarman.mockito_kotlin.mock

class MockLoggerFactory : LoggerFactory {

    override fun getLogger(tag: String): Logger = mock()

}