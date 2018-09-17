package com.lelloman.deviceinfo.ui

import android.arch.lifecycle.MutableLiveData
import com.lelloman.common.utils.ActionTokenProvider
import com.lelloman.common.view.ResourceProvider
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.deviceinfo.infoitem.InfoItem

abstract class InfoListViewModel(
    resourceProvider: ResourceProvider,
    actionTokenProvider: ActionTokenProvider
) : BaseViewModel(
    resourceProvider = resourceProvider,
    actionTokenProvider = actionTokenProvider
) {

    abstract val hello: String

    abstract val deviceInfos: MutableLiveData<List<InfoItem>>
}