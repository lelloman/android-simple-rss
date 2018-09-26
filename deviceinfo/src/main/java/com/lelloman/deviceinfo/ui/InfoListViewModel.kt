package com.lelloman.deviceinfo.ui

import android.arch.lifecycle.MutableLiveData
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.deviceinfo.infoitem.InfoItem

abstract class InfoListViewModel(dependencies: Dependencies) : BaseViewModel(dependencies) {

    abstract val hello: String

    abstract val deviceInfos: MutableLiveData<List<InfoItem>>

    abstract fun onInfoItemClicked(infoItem: InfoItem)
}