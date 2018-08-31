package com.lelloman.read.core.view

import android.support.annotation.StyleRes
import com.lelloman.read.R
import com.lelloman.read.utils.Constants.AppSettings.DEFAULT_APP_THEME

enum class AppTheme(
    @StyleRes val resId: Int
) {
    LIGHT(R.style.CustomTheme_Light),
    DARCULA(R.style.CustomTheme_Darcula);

    companion object {
        private val namesMap = AppTheme
            .values()
            .associateBy(AppTheme::name)

        fun fromName(name: String): AppTheme =
            if (namesMap.containsKey(name)) {
                namesMap[name]!!
            } else {
                DEFAULT_APP_THEME
            }
    }
}