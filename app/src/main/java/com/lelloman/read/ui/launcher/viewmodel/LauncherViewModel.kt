package com.lelloman.read.ui.launcher.viewmodel

import com.lelloman.common.utils.ActionTokenProvider
import com.lelloman.common.view.ResourceProvider
import com.lelloman.read.core.viewmodel.BaseViewModel

abstract class LauncherViewModel(
    actionTokenProvider: ActionTokenProvider,
    resourceProvider: ResourceProvider
) : BaseViewModel(
    resourceProvider = resourceProvider,
    actionTokenProvider = actionTokenProvider
)