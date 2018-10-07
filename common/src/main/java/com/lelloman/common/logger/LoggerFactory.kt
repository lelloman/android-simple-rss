package com.lelloman.common.logger

interface LoggerFactory {

    @Deprecated(message = "Use getLogger(Class) instead.")
    fun getLogger(tag: String): Logger

    fun getLogger(clazz: Class<*>): Logger
}