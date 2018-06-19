package com.lelloman.read.persistence.settings

import android.content.Context
import com.lelloman.read.utils.Constants.AppSettings.KEY_MIN_SOURCE_REFRESH_INTERVAL
import com.lelloman.read.utils.Constants.AppSettings.SHARED_PREFS_NAME

class AppSettingsImpl(
    private val context: Context
) : AppSettings {

    private val prefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    override var sourceRefreshMinInterval: SourceRefreshInterval
        get() = prefs
            .getString(KEY_MIN_SOURCE_REFRESH_INTERVAL, "")
            .let { SourceRefreshInterval.fromName(it) }
        set(value) = prefs
            .edit()
            .putString(KEY_MIN_SOURCE_REFRESH_INTERVAL, value.name)
            .apply()


}