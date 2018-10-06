package com.lelloman.common.view

import android.graphics.drawable.Drawable
import android.support.annotation.ArrayRes
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes

interface ResourceProvider {
    fun getString(@StringRes stringId: Int, vararg args: Any): String
    fun getStringArray(@ArrayRes arrayId: Int): Array<String>
    fun getDrawable(@DrawableRes drawableId: Int): Drawable
}