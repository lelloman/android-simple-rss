package com.lelloman.read.core

interface TimeProvider {

    fun nowUtcMs(): Long
}