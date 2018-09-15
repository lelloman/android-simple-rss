package com.lelloman.common.settings

import android.content.Context
import com.lelloman.common.settings.BaseApplicationSettings.Companion.DEFAULT_APP_THEME
import com.lelloman.common.settings.BaseApplicationSettings.Companion.DEFAULT_USE_METERED_NETWORK
import com.lelloman.common.settings.BaseApplicationSettings.Companion.KEY_APP_THEME
import com.lelloman.common.settings.BaseApplicationSettings.Companion.KEY_USE_METERED_NETWORK
import com.lelloman.common.settings.BaseApplicationSettings.Companion.SHARED_PREFS_NAME
import com.lelloman.common.settings.property.booleanProperty
import com.lelloman.common.settings.property.enumProperty
import com.lelloman.common.view.AppTheme

class BaseApplicationSettingsImpl(
    context: Context
) : BaseApplicationSettings {

    private val prefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    private val useMeteredNetworkProperty = prefs
        .booleanProperty(KEY_USE_METERED_NETWORK, DEFAULT_USE_METERED_NETWORK)

    private val appThemeProperty = prefs
        .enumProperty(KEY_APP_THEME, DEFAULT_APP_THEME, AppTheme.Companion::fromName)

    override val useMeteredNetwork = useMeteredNetworkProperty.observable
    override val appTheme = appThemeProperty.observable

    override fun readAllSettings() {
        useMeteredNetworkProperty.readValue()
        appThemeProperty.readValue()
    }

    override fun reset() {
        prefs.edit().clear().apply()
        readAllSettings()
    }

    override fun setUseMeteredNetwork(useMeteredNetwork: Boolean) =
        useMeteredNetworkProperty.set(useMeteredNetwork)

    override fun setAppTheme(appTheme: AppTheme) = appThemeProperty.set(appTheme)

}