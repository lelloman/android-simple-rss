package com.lelloman.read.core

import android.content.Context
import android.support.annotation.StringRes
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourceProvider @Inject constructor(private val context: Context) {

    fun getString(@StringRes stringId: Int, args: Array<Any> = emptyArray()): String =
        context.getString(stringId, args)
}