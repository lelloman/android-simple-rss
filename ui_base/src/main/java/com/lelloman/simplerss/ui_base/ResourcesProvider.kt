package com.lelloman.simplerss.ui_base

import androidx.annotation.StringRes

interface ResourcesProvider {

    fun getString(@StringRes resId: Int): String

    fun getString(@StringRes resId: Int, vararg fmt: Any): String

    fun nowUtcMs(): Long
}