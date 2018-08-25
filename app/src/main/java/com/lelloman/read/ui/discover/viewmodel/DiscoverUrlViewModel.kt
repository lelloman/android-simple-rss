package com.lelloman.read.ui.discover.viewmodel

import com.lelloman.read.core.ActionTokenProvider
import com.lelloman.read.core.ResourceProvider
import com.lelloman.read.core.viewmodel.BaseViewModel

abstract class DiscoverUrlViewModel(
    resourceProvider: ResourceProvider,
    actionTokenProvider: ActionTokenProvider
) : BaseViewModel(
    resourceProvider = resourceProvider,
    actionTokenProvider = actionTokenProvider
)