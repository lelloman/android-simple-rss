package com.lelloman.common.utils

interface TimeProvider {

    fun nowUtcMs(): Long
}