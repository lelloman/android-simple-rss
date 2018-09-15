package com.lelloman.common.utils

import java.util.*

class ActionTokenProvider {
    fun makeActionToken() = UUID.randomUUID().toString()
}