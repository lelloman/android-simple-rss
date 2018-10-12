package com.lelloman.common.logger

interface LoggerFactory {

    fun getLogger(clazz: Class<*>): Logger
}