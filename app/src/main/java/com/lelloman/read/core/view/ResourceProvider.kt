package com.lelloman.read.core.view

import android.support.annotation.ArrayRes
import android.support.annotation.StringRes

interface ResourceProvider {
    fun getString(@StringRes stringId: Int, vararg args: Any): String
    fun getStringArray(@ArrayRes arrayId: Int): Array<String>
}