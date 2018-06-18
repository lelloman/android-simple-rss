package com.lelloman.read.core

import android.support.annotation.StringRes

interface ResourceProvider {
    fun getString(@StringRes stringId: Int, vararg args: Any): String
}