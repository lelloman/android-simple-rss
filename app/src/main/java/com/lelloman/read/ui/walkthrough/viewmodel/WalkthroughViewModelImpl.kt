package com.lelloman.read.ui.walkthrough.viewmodel

import com.lelloman.read.core.ActionTokenProvider
import com.lelloman.read.core.ResourceProvider

class WalkthroughViewModelImpl(
    resourceProvider: ResourceProvider,
    actionTokenProvider: ActionTokenProvider
) : WalkthroughViewModel(
    resourceProvider = resourceProvider,
    actionTokenProvider = actionTokenProvider
)