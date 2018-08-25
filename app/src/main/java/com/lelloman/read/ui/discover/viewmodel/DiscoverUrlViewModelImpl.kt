package com.lelloman.read.ui.discover.viewmodel

import com.lelloman.read.core.ActionTokenProvider
import com.lelloman.read.core.ResourceProvider

class DiscoverUrlViewModelImpl(
    resourceProvider: ResourceProvider,
    actionTokenProvider: ActionTokenProvider
) : DiscoverUrlViewModel(
    resourceProvider = resourceProvider,
    actionTokenProvider = actionTokenProvider
)