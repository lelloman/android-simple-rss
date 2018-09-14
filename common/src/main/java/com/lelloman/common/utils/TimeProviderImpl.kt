package com.lelloman.common.utils

class TimeProviderImpl : TimeProvider {

    override fun nowUtcMs() = System.currentTimeMillis()
}