package com.lelloman.read.core

import java.util.*

class ActionTokenProvider {
    fun makeActionToken() = UUID.randomUUID().toString()
}