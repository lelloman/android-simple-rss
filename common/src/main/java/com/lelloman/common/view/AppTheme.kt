package com.lelloman.common.view

import android.support.annotation.StyleRes
import com.lelloman.common.R
import com.lelloman.common.utils.Named

enum class AppTheme(
    @StyleRes val resId: Int
) : Named {
    LIGHT(R.style.CustomTheme_Light),
    DARCULA(R.style.CustomTheme_Darcula),
    MOCKITO(R.style.CustomTheme_Mockito),
    BLACK(R.style.CustomTheme_Black),
    FOREST(R.style.CustomTheme_Forest);

    companion object {

        val DEFAULT = LIGHT

        private val namesMap = AppTheme
            .values()
            .associateBy(AppTheme::name)

        fun fromName(name: String): AppTheme =
            if (namesMap.containsKey(name)) {
                namesMap[name]!!
            } else {
                DEFAULT
            }
    }
}