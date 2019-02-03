package com.lelloman.simplerss.ui.launcher.viewmodel

class LauncherViewModelImpl(
    private val appSettings: com.lelloman.simplerss.persistence.settings.AppSettings,
    dependencies: Dependencies
) : com.lelloman.simplerss.ui.launcher.viewmodel.LauncherViewModel(dependencies) {

    override fun onViewShown() {
        super.onViewShown()
        if (appSettings.shouldShowWalkthrough.blockingFirst()) {
            navigateAndClose(com.lelloman.simplerss.navigation.SimpleRssNavigationScreen.WALKTHROUGH)
        } else {
            navigateAndClose(com.lelloman.simplerss.navigation.SimpleRssNavigationScreen.ARTICLES_LIST)
        }
    }
}