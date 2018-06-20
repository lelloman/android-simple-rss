package com.lelloman.read.core.logger

interface LoggerFactory {
    fun getLogger(tag: String): Logger
}