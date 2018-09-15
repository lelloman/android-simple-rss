package com.lelloman.common.logger

interface Logger {
    fun i(msg: String)
    fun d(msg: String)
    fun w(msg: String, throwable: Throwable? = null)
    fun e(msg: String, throwable: Throwable? = null)
}