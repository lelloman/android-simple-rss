package com.lelloman.simplerss.ui.launcher.viewmodel

import com.lelloman.common.viewmodel.command.CloseScreenCommand
import com.lelloman.simplerss.persistence.settings.AppSettings
import com.lelloman.simplerss.ui.OpenArticlesListScreenCommand
import com.lelloman.simplerss.ui.OpenWalkthroughScreenCommand

class LauncherViewModelImpl(
    private val appSettings: AppSettings,
    dependencies: Dependencies
) : LauncherViewModel(dependencies) {

    override fun onViewLoaded() {
        if (appSettings.shouldShowWalkthrough.blockingFirst()) {
            emitCommand(OpenWalkthroughScreenCommand)
        } else {
            emitCommand(OpenArticlesListScreenCommand)
        }
        emitCommand(CloseScreenCommand)
    }
}