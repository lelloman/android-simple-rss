package com.lelloman.read.core.logger

class LoggerFactoryImpl : LoggerFactory {

    override fun getLogger(tag: String): Logger {
        return AndroidLogger(tag)
    }
}