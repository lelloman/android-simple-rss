package com.lelloman.simplerss.persistence.settings

import android.content.Context
import com.lelloman.common.settings.BaseApplicationSettings
import com.lelloman.common.settings.property.booleanProperty
import com.lelloman.common.settings.property.enumProperty
import com.lelloman.simplerss.persistence.settings.AppSettings.Companion.DEFAULT_ARTICLES_LIST_IMAGES
import com.lelloman.simplerss.persistence.settings.AppSettings.Companion.DEFAULT_MIN_SOURCE_REFRESH_INTERVAL
import com.lelloman.simplerss.persistence.settings.AppSettings.Companion.DEFAULT_OPEN_ARTICLES_IN_APP
import com.lelloman.simplerss.persistence.settings.AppSettings.Companion.DEFAULT_SHOULD_SHOW_WALKTHROUGH
import com.lelloman.simplerss.persistence.settings.AppSettings.Companion.KEY_ARTICLE_LIST_IMAGES
import com.lelloman.simplerss.persistence.settings.AppSettings.Companion.KEY_MIN_SOURCE_REFRESH_INTERVAL
import com.lelloman.simplerss.persistence.settings.AppSettings.Companion.KEY_OPEN_ARTICLES_IN_APP
import com.lelloman.simplerss.persistence.settings.AppSettings.Companion.KEY_SHOULD_SHOW_WALKTHROUGH
import com.lelloman.simplerss.persistence.settings.AppSettings.Companion.SHARED_PREFS_NAME
import com.lelloman.simplerss.persistence.settings.SourceRefreshInterval.Companion.fromName

class AppSettingsImpl(
    context: Context,
    private val baseApplicationSettings: BaseApplicationSettings
) : AppSettings, BaseApplicationSettings by baseApplicationSettings {

    private val prefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    private val sourceRefreshMinIntervalProperty = prefs
        .enumProperty(KEY_MIN_SOURCE_REFRESH_INTERVAL, DEFAULT_MIN_SOURCE_REFRESH_INTERVAL, ::fromName)

    private val articlesListImagesEnabledProperty = prefs
        .booleanProperty(KEY_ARTICLE_LIST_IMAGES, DEFAULT_ARTICLES_LIST_IMAGES)

    private val openArticlesInAppProperty = prefs
        .booleanProperty(KEY_OPEN_ARTICLES_IN_APP, DEFAULT_OPEN_ARTICLES_IN_APP)

    private val shouldShowWalkthroughProperty = prefs
        .booleanProperty(KEY_SHOULD_SHOW_WALKTHROUGH, DEFAULT_SHOULD_SHOW_WALKTHROUGH)

    override val sourceRefreshMinInterval = sourceRefreshMinIntervalProperty.observable
    override val articleListImagesEnabled = articlesListImagesEnabledProperty.observable
    override val openArticlesInApp = openArticlesInAppProperty.observable
    override val shouldShowWalkthrough = shouldShowWalkthroughProperty.observable

    init {
        readAllSettings()
    }

    override fun readAllSettings() {
        baseApplicationSettings.readAllSettings()
        sourceRefreshMinIntervalProperty.readValue()
        articlesListImagesEnabledProperty.readValue()
        openArticlesInAppProperty.readValue()
        shouldShowWalkthroughProperty.readValue()
    }

    override fun reset() {
        baseApplicationSettings.reset()
        prefs.edit().clear().commit()
        readAllSettings()
    }

    override fun setSourceRefreshMinInterval(interval: SourceRefreshInterval) =
        sourceRefreshMinIntervalProperty.set(interval)

    override fun setArticlesListImagesEnabled(enabled: Boolean) =
        articlesListImagesEnabledProperty.set(enabled)

    override fun setOpenArticlesInApp(openInApp: Boolean) =
        openArticlesInAppProperty.set(openInApp)

    override fun setShouldShowWalkthtough(shouldShowWalkthrough: Boolean) =
        shouldShowWalkthroughProperty.set(shouldShowWalkthrough)
}