package com.lelloman.read.persistence.settings

import android.content.Context
import com.lelloman.read.utils.Constants.AppSettings.DEFAULT_ARTICLES_LIST_IMaGES
import com.lelloman.read.utils.Constants.AppSettings.DEFAULT_USE_METERED_NETWORK
import com.lelloman.read.utils.Constants.AppSettings.KEY_ARTICLE_LIST_IMAGES
import com.lelloman.read.utils.Constants.AppSettings.KEY_MIN_SOURCE_REFRESH_INTERVAL
import com.lelloman.read.utils.Constants.AppSettings.KEY_USE_METERED_NETWORK
import com.lelloman.read.utils.Constants.AppSettings.SHARED_PREFS_NAME

class AppSettingsImpl(
    context: Context
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

    override var articleListImages: Boolean
        get() = prefs
            .getBoolean(KEY_ARTICLE_LIST_IMAGES, DEFAULT_ARTICLES_LIST_IMaGES)
        set(value) = prefs
            .edit()
            .putBoolean(KEY_ARTICLE_LIST_IMAGES, value)
            .apply()

    override var useMeteredNetwork: Boolean
        get() = prefs
            .getBoolean(KEY_USE_METERED_NETWORK, DEFAULT_USE_METERED_NETWORK)
        set(value) = prefs
            .edit()
            .putBoolean(KEY_USE_METERED_NETWORK, value)
            .apply()
}