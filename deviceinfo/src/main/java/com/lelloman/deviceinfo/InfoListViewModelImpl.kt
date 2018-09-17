package com.lelloman.deviceinfo

import com.lelloman.common.utils.ActionTokenProvider
import com.lelloman.common.view.ResourceProvider

class InfoListViewModelImpl(
    resourceProvider: ResourceProvider,
    actionTokenProvider: ActionTokenProvider
) : InfoListViewModel(
    resourceProvider = resourceProvider,
    actionTokenProvider = actionTokenProvider
) {
    override val hello = "asdasdasd"
}