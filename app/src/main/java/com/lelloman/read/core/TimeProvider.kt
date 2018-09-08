package com.lelloman.read.core

open class TimeProvider {

    open fun nowUtcMs() = System.currentTimeMillis()
}