package com.lelloman.common.view

interface SemanticTimeProvider {
    fun getDateTimeString(time: Long): String
    fun getTimeQuantity(ms: Long): String
    fun getTimeDiffString(timeUtcMs: Long): String
}