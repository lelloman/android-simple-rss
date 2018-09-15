package com.lelloman.read.persistence.settings

import com.lelloman.common.view.AppTheme
import io.reactivex.Observable

interface AppSettings {

    val sourceRefreshMinInterval: Observable<SourceRefreshInterval>

    val articleListImagesEnabled: Observable<Boolean>

    val useMeteredNetwork: Observable<Boolean>

    val openArticlesInApp: Observable<Boolean>

    val shouldShowWalkthrough: Observable<Boolean>

    val appTheme: Observable<AppTheme>

    fun reset()

    fun setSourceRefreshMinInterval(interval: SourceRefreshInterval)

    fun setArticlesListImagesEnabled(enabled: Boolean)

    fun setUseMeteredNetwork(useMeteredNetwork: Boolean)

    fun setOpenArticlesInApp(openInApp: Boolean)

    fun setShouldShowWalkthtough(shouldShowWalkthrough: Boolean)

    fun setAppTheme(appTheme: AppTheme)
}