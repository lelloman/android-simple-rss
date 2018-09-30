package com.lelloman.read.mock

import com.lelloman.common.view.AppTheme
import com.lelloman.read.persistence.settings.AppSettings
import com.lelloman.read.persistence.settings.SourceRefreshInterval
import io.reactivex.Observable

class MockAppSettings(
    var providedSourceRefreshMinInterval: Observable<SourceRefreshInterval> = Observable.empty(),
    var providedArticleListImagesEnabled: Observable<Boolean> = Observable.empty(),
    var providedUseMeteredNetwork: Observable<Boolean> = Observable.empty(),
    var providedOpenArticlesInApp: Observable<Boolean> = Observable.empty(),
    private var providedShouldShowWalkthrough: Observable<Boolean> = Observable.empty(),
    private var providedAppTheme: Observable<AppTheme> = Observable.empty()
) : AppSettings {

    override val sourceRefreshMinInterval: Observable<SourceRefreshInterval> get() = providedSourceRefreshMinInterval
    override val articleListImagesEnabled: Observable<Boolean> get() = providedArticleListImagesEnabled
    override val useMeteredNetwork: Observable<Boolean> get() = providedUseMeteredNetwork
    override val openArticlesInApp: Observable<Boolean> get() = providedOpenArticlesInApp
    override val shouldShowWalkthrough: Observable<Boolean> get() = providedShouldShowWalkthrough
    override val appTheme: Observable<AppTheme> get() = providedAppTheme

    override fun reset() = Unit

    override fun readAllSettings() = Unit

    override fun setSourceRefreshMinInterval(interval: SourceRefreshInterval) = Unit

    override fun setArticlesListImagesEnabled(enabled: Boolean) = Unit

    override fun setUseMeteredNetwork(useMeteredNetwork: Boolean) = Unit

    override fun setOpenArticlesInApp(openInApp: Boolean) = Unit

    override fun setShouldShowWalkthtough(shouldShowWalkthrough: Boolean) = Unit

    override fun setAppTheme(appTheme: AppTheme) = Unit

}