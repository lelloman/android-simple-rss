package com.lelloman.read.persistence.settings

import android.content.Context
import com.lelloman.read.core.view.AppTheme
import com.lelloman.read.persistence.settings.property.booleanProperty
import com.lelloman.read.persistence.settings.property.enumProperty
import com.lelloman.read.utils.Constants.AppSettings.DEFAULT_APP_THEME
import com.lelloman.read.utils.Constants.AppSettings.DEFAULT_ARTICLES_LIST_IMAGES
import com.lelloman.read.utils.Constants.AppSettings.DEFAULT_MIN_SOURCE_REFRESH_INTERVAL
import com.lelloman.read.utils.Constants.AppSettings.DEFAULT_OPEN_ARTICLES_IN_APP
import com.lelloman.read.utils.Constants.AppSettings.DEFAULT_SHOULD_SHOW_WALKTHROUGH
import com.lelloman.read.utils.Constants.AppSettings.DEFAULT_USE_METERED_NETWORK
import com.lelloman.read.utils.Constants.AppSettings.KEY_APP_THEME
import com.lelloman.read.utils.Constants.AppSettings.KEY_ARTICLE_LIST_IMAGES
import com.lelloman.read.utils.Constants.AppSettings.KEY_MIN_SOURCE_REFRESH_INTERVAL
import com.lelloman.read.utils.Constants.AppSettings.KEY_OPEN_ARTICLES_IN_APP
import com.lelloman.read.utils.Constants.AppSettings.KEY_SHOULD_SHOW_WALKTHROUGH
import com.lelloman.read.utils.Constants.AppSettings.KEY_USE_METERED_NETWORK
import com.lelloman.read.utils.Constants.AppSettings.SHARED_PREFS_NAME

class AppSettingsImpl(
    context: Context
) : AppSettings {

    private val prefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    private val sourceRefreshMinIntervalProperty = prefs
        .enumProperty(KEY_MIN_SOURCE_REFRESH_INTERVAL, DEFAULT_MIN_SOURCE_REFRESH_INTERVAL, SourceRefreshInterval.Companion::fromName)

    private val articlesListImagesEnabledProperty = prefs
        .booleanProperty(KEY_ARTICLE_LIST_IMAGES, DEFAULT_ARTICLES_LIST_IMAGES)

    private val useMeteredNetworkProperty = prefs
        .booleanProperty(KEY_USE_METERED_NETWORK, DEFAULT_USE_METERED_NETWORK)

    private val openArticlesInAppProperty = prefs
        .booleanProperty(KEY_OPEN_ARTICLES_IN_APP, DEFAULT_OPEN_ARTICLES_IN_APP)

    private val shouldShowWalkthroughProperty = prefs
        .booleanProperty(KEY_SHOULD_SHOW_WALKTHROUGH, DEFAULT_SHOULD_SHOW_WALKTHROUGH)

    private val appThemeProperty = prefs
        .enumProperty(KEY_APP_THEME, DEFAULT_APP_THEME, AppTheme.Companion::fromName)

    override val sourceRefreshMinInterval = sourceRefreshMinIntervalProperty.observable
    override val articleListImagesEnabled = articlesListImagesEnabledProperty.observable
    override val useMeteredNetwork = useMeteredNetworkProperty.observable
    override val openArticlesInApp = openArticlesInAppProperty.observable
    override val shouldShowWalkthrough = shouldShowWalkthroughProperty.observable
    override val appTheme = appThemeProperty.observable

    init {
        readAllSettings()
    }

    private fun readAllSettings() {
        sourceRefreshMinIntervalProperty.readValue()
        articlesListImagesEnabledProperty.readValue()
        useMeteredNetworkProperty.readValue()
        openArticlesInAppProperty.readValue()
        shouldShowWalkthroughProperty.readValue()
        appThemeProperty.readValue()
    }

    override fun reset() {
        prefs.edit().clear().apply()
        readAllSettings()
    }

    override fun setSourceRefreshMinInterval(interval: SourceRefreshInterval) =
        sourceRefreshMinIntervalProperty.set(interval)

    override fun setArticlesListImagesEnabled(enabled: Boolean) =
        articlesListImagesEnabledProperty.set(enabled)

    override fun setUseMeteredNetwork(useMeteredNetwork: Boolean) =
        useMeteredNetworkProperty.set(useMeteredNetwork)

    override fun setOpenArticlesInApp(openInApp: Boolean) =
        openArticlesInAppProperty.set(openInApp)

    override fun setShouldShowWalkthtough(shouldShowWalkthrough: Boolean) =
        shouldShowWalkthroughProperty.set(shouldShowWalkthrough)

    override fun setAppTheme(appTheme: AppTheme) = appThemeProperty.set(appTheme)
}