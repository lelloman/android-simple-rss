package com.lelloman.simplerss.ui.launcher.viewmodel

import com.lelloman.common.viewmodel.BaseViewModel

abstract class LauncherViewModel(dependencies: Dependencies) : BaseViewModel(dependencies) {
    abstract fun onViewLoaded()
}