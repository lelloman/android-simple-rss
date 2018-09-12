package com.lelloman.read.ui.launcher.viewmodel

import com.lelloman.read.core.ActionTokenProvider
import com.lelloman.read.core.navigation.NavigationScreen
import com.lelloman.read.core.navigation.ScreenAndCloseNavigationEvent
import com.lelloman.read.core.view.ResourceProvider
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
            navigate(ScreenAndCloseNavigationEvent(NavigationScreen.WALKTHROUGH))
        } else {
            navigateAndClose(NavigationScreen.ARTICLES_LIST)
        }
    }
}