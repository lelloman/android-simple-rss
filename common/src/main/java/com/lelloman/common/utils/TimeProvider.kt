package com.lelloman.common.utils

import com.lelloman.common.utils.model.Time

interface TimeProvider {

    fun nowUtcMs(): Long

    fun now(): Time

    fun getTime(utcMs: Long): Time
}