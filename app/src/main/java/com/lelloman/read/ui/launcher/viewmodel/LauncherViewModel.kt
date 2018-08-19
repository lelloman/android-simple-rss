package com.lelloman.read.ui.launcher.viewmodel

import com.lelloman.read.core.ActionTokenProvider
import com.lelloman.read.core.ResourceProvider
import com.lelloman.read.core.viewmodel.BaseViewModel

abstract class LauncherViewModel(
    actionTokenProvider: ActionTokenProvider,
    resourceProvider: ResourceProvider
) : BaseViewModel(
    resourceProvider = resourceProvider,
    actionTokenProvider = actionTokenProvider
)