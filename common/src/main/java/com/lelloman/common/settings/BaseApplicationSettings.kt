package com.lelloman.common.settings

import com.lelloman.common.view.AppTheme
import io.reactivex.Observable

interface BaseApplicationSettings {

    val useMeteredNetwork: Observable<Boolean>

    val appTheme: Observable<AppTheme>

    fun readAllSettings()

    fun reset()

    fun setUseMeteredNetwork(useMeteredNetwork: Boolean)

    fun setAppTheme(appTheme: AppTheme)

    companion object {
        const val SHARED_PREFS_NAME = "BaseApplicationSettings"

        const val KEY_USE_METERED_NETWORK = "UseMeteredNetwork"
        const val DEFAULT_USE_METERED_NETWORK = false

        const val KEY_APP_THEME = "AppTheme"
        val DEFAULT_APP_THEME = AppTheme.DEFAULT
    }
}