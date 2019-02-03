package com.lelloman.read.persistence.settings

import com.lelloman.common.settings.BaseApplicationSettings
import io.reactivex.Observable

interface AppSettings : BaseApplicationSettings {

    val sourceRefreshMinInterval: Observable<SourceRefreshInterval>

    val articleListImagesEnabled: Observable<Boolean>

    val openArticlesInApp: Observable<Boolean>

    val shouldShowWalkthrough: Observable<Boolean>

    fun setSourceRefreshMinInterval(interval: SourceRefreshInterval)

    fun setArticlesListImagesEnabled(enabled: Boolean)

    fun setOpenArticlesInApp(openInApp: Boolean)

    fun setShouldShowWalkthtough(shouldShowWalkthrough: Boolean)

    companion object {
        const val SHARED_PREFS_NAME = "AppSettings"

        const val KEY_MIN_SOURCE_REFRESH_INTERVAL = "MinSourceRefreshInterval"
        val DEFAULT_MIN_SOURCE_REFRESH_INTERVAL = SourceRefreshInterval.VERY_FREQUENT

        const val KEY_ARTICLE_LIST_IMAGES = "ArticleListImages"
        const val DEFAULT_ARTICLES_LIST_IMAGES = true

        const val KEY_OPEN_ARTICLES_IN_APP = "OpenArticlesInApp"
        const val DEFAULT_OPEN_ARTICLES_IN_APP = false

        const val KEY_SHOULD_SHOW_WALKTHROUGH = "ShouldShowWalkthrough"
        const val DEFAULT_SHOULD_SHOW_WALKTHROUGH = true
    }
}