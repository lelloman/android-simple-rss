package com.lelloman.read.core.view

import android.support.annotation.StyleRes
import com.lelloman.read.R
import com.lelloman.read.utils.Constants.AppSettings.DEFAULT_APP_THEME
import com.lelloman.read.utils.Named

enum class AppTheme(
    @StyleRes val resId: Int
) : Named {
    LIGHT(R.style.CustomTheme_Light),
    DARCULA(R.style.CustomTheme_Darcula),
    MOCKITO(R.style.CustomTheme_Mockito),
    BLACK(R.style.CustomTheme_Black),
    FOREST(R.style.CustomTheme_Forest);

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