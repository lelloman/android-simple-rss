package com.lelloman.simplerss.ui.launcher.viewmodel

import com.lelloman.simplerss.navigation.SimpleRssNavigationScreen
import com.lelloman.simplerss.persistence.settings.AppSettings

class LauncherViewModelImpl(
    private val appSettings: AppSettings,
    dependencies: Dependencies
) : LauncherViewModel(dependencies) {

    override fun onViewLoaded() {
        if (appSettings.shouldShowWalkthrough.blockingFirst()) {
            navigateAndClose(SimpleRssNavigationScreen.WALKTHROUGH)
        } else {
            navigateAndClose(SimpleRssNavigationScreen.ARTICLES_LIST)
        }
    }
}