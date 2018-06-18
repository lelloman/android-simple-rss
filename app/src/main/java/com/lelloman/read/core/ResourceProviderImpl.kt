package com.lelloman.read.core

import android.content.Context
import android.support.annotation.StringRes

class ResourceProviderImpl(private val context: Context) : ResourceProvider {

    override fun getString(@StringRes stringId: Int, vararg args: Any): String =
        context.getString(stringId, *args)
}