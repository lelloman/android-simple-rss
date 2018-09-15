package com.lelloman.common.logger

interface LoggerFactory {
    fun getLogger(tag: String): Logger
}