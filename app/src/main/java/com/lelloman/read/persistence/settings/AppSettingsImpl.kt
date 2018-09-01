package com.lelloman.read.persistence.settings

import android.content.Context
import com.lelloman.read.core.view.AppTheme
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
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject

class AppSettingsImpl(
    context: Context
) : AppSettings {

    private val prefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    private val sourceRefreshMinIntervalSubject: Subject<SourceRefreshInterval> = BehaviorSubject.create()

    private val articlesListImagesEnabledSubject: Subject<Boolean> = BehaviorSubject.create()

    private val useMeteredNetworkSubject: Subject<Boolean> = BehaviorSubject.create()

    private val openArticlesInAppSubject: Subject<Boolean> = BehaviorSubject.create()

    private val shouldShowWalkthroughSubject: Subject<Boolean> = BehaviorSubject.create()

    private val appThemeSubject: Subject<AppTheme> = BehaviorSubject.create()

    override val sourceRefreshMinInterval: Observable<SourceRefreshInterval> =
        sourceRefreshMinIntervalSubject.hide()

    override val articleListImagesEnabled: Observable<Boolean> =
        articlesListImagesEnabledSubject.hide()

    override val useMeteredNetwork: Observable<Boolean> =
        useMeteredNetworkSubject.hide()

    override val openArticlesInApp: Observable<Boolean> =
        openArticlesInAppSubject.hide()

    override val shouldShowWalkthrough: Observable<Boolean> =
        shouldShowWalkthroughSubject.hide()

    override val appTheme: Observable<AppTheme> = appThemeSubject.hide()

    init {
        readAllSettings()
    }

    private fun readAllSettings() {
        sourceRefreshMinIntervalSubject.onNext(
            prefs
                .getString(KEY_MIN_SOURCE_REFRESH_INTERVAL, DEFAULT_MIN_SOURCE_REFRESH_INTERVAL.name)
                .let { SourceRefreshInterval.fromName(it) }
        )
        articlesListImagesEnabledSubject.onNext(
            prefs.getBoolean(KEY_ARTICLE_LIST_IMAGES, DEFAULT_ARTICLES_LIST_IMAGES)
        )
        useMeteredNetworkSubject.onNext(
            prefs.getBoolean(KEY_USE_METERED_NETWORK, DEFAULT_USE_METERED_NETWORK)
        )
        openArticlesInAppSubject.onNext(
            prefs.getBoolean(KEY_OPEN_ARTICLES_IN_APP, DEFAULT_OPEN_ARTICLES_IN_APP)
        )

        shouldShowWalkthroughSubject.onNext(
            prefs.getBoolean(KEY_SHOULD_SHOW_WALKTHROUGH, DEFAULT_SHOULD_SHOW_WALKTHROUGH)
        )
        appThemeSubject.onNext(
            prefs
                .getString(KEY_APP_THEME, DEFAULT_APP_THEME.name)
                .let { AppTheme.fromName(it) }
        )
    }

    override fun reset() {
        prefs.edit().clear().apply()
        readAllSettings()
    }

    override fun setSourceRefreshMinInterval(interval: SourceRefreshInterval) = prefs
        .edit()
        .putString(KEY_MIN_SOURCE_REFRESH_INTERVAL, interval.name)
        .apply()
        .also { sourceRefreshMinIntervalSubject.onNext(interval) }

    override fun setArticlesListImagesEnabled(enabled: Boolean) = prefs
        .edit()
        .putBoolean(KEY_ARTICLE_LIST_IMAGES, enabled)
        .apply()
        .also { articlesListImagesEnabledSubject.onNext(enabled) }

    override fun setUseMeteredNetwork(useMeteredNetwork: Boolean) = prefs
        .edit()
        .putBoolean(KEY_USE_METERED_NETWORK, useMeteredNetwork)
        .apply()
        .also { useMeteredNetworkSubject.onNext(useMeteredNetwork) }

    override fun setOpenArticlesInApp(openInApp: Boolean) = prefs
        .edit()
        .putBoolean(KEY_OPEN_ARTICLES_IN_APP, openInApp)
        .apply()
        .also { openArticlesInAppSubject.onNext(openInApp) }

    override fun setShouldShowWalkthtough(shouldShowWalkthrough: Boolean) = prefs
        .edit()
        .putBoolean(KEY_SHOULD_SHOW_WALKTHROUGH, shouldShowWalkthrough)
        .apply()
        .also { shouldShowWalkthroughSubject.onNext(shouldShowWalkthrough) }

    override fun setAppTheme(appTheme: AppTheme) = prefs
        .edit()
        .putString(KEY_APP_THEME, appTheme.name)
        .apply()
        .also { appThemeSubject.onNext(appTheme) }
}