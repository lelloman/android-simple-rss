package com.lelloman.read.ui.launcher.viewmodel

import com.lelloman.common.utils.ActionTokenProvider
import com.lelloman.common.view.ResourceProvider
import com.lelloman.read.core.navigation.ReadNavigationScreen
import com.lelloman.read.persistence.settings.AppSettings

class LauncherViewModelImpl(
    actionTokenProvider: ActionTokenProvider,
    resourceProvider: ResourceProvider,
    private val appSettings: AppSettings
) : LauncherViewModel(
    resourceProvider = resourceProvider,
    actionTokenProvider = actionTokenProvider
) {

    override fun onCreate() {
        if (appSettings.shouldShowWalkthrough.blockingFirst()) {
            navigateAndClose(ReadNavigationScreen.WALKTHROUGH)
        } else {
            navigateAndClose(ReadNavigationScreen.ARTICLES_LIST)
        }
    }
}