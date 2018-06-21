package com.lelloman.read.persistence.settings

import android.content.Context
import com.lelloman.read.utils.Constants.AppSettings.DEFAULT_ARTICLES_LIST_IMAGES
import com.lelloman.read.utils.Constants.AppSettings.DEFAULT_MIN_SOURCE_REFRESH_INTERVAL
import com.lelloman.read.utils.Constants.AppSettings.DEFAULT_USE_METERED_NETWORK
import com.lelloman.read.utils.Constants.AppSettings.KEY_ARTICLE_LIST_IMAGES
import com.lelloman.read.utils.Constants.AppSettings.KEY_MIN_SOURCE_REFRESH_INTERVAL
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

    override val sourceRefreshMinInterval: Observable<SourceRefreshInterval> =
        sourceRefreshMinIntervalSubject.hide()

    override val articleListImagesEnabled: Observable<Boolean> =
        articlesListImagesEnabledSubject.hide()

    override val useMeteredNetwork: Observable<Boolean> =
        useMeteredNetworkSubject.hide()

    init {
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
}