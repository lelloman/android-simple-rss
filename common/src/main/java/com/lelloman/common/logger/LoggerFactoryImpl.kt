package com.lelloman.common.logger

class LoggerFactoryImpl : LoggerFactory {

    override fun getLogger(tag: String): Logger {
        return AndroidLogger(tag)
    }
}