package com.lelloman.simplerss.viewmodel

import android.content.Context
import com.lelloman.simplerss.ui_base.ResourcesProvider

class ContextResourcesProvider(private val context: Context) : ResourcesProvider {

    override fun getString(resId: Int): String {
        return context.getString(resId)
    }

    override fun getString(resId: Int, vararg fmt: Any): String {
        return context.getString(resId, *fmt)
    }

    override fun nowUtcMs() = System.currentTimeMillis()
}