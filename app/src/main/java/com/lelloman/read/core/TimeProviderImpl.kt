package com.lelloman.read.core

class TimeProviderImpl : TimeProvider {

    override fun nowUtcMs() = System.currentTimeMillis()
}