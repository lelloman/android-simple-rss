package com.lelloman.common.logger

class LoggerFactoryImpl : LoggerFactory {

    override fun getLogger(clazz: Class<*>): Logger {
        return AndroidLogger(clazz.simpleName)
    }
}