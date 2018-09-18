package com.lelloman.read.ui.launcher.viewmodel

import com.lelloman.read.navigation.ReadNavigationScreen
import com.lelloman.read.persistence.settings.AppSettings

class LauncherViewModelImpl(
    private val appSettings: AppSettings,
    dependencies: Dependencies
) : LauncherViewModel(dependencies) {

    override fun onCreate() {
        if (appSettings.shouldShowWalkthrough.blockingFirst()) {
            navigateAndClose(ReadNavigationScreen.WALKTHROUGH)
        } else {
            navigateAndClose(ReadNavigationScreen.ARTICLES_LIST)
        }
    }
}