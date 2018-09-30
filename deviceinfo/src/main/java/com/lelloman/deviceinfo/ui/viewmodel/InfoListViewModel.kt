package com.lelloman.deviceinfo.ui.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.deviceinfo.infoitem.InfoItem

abstract class InfoListViewModel(dependencies: Dependencies) : BaseViewModel(dependencies) {

    abstract val deviceInfos: MutableLiveData<List<InfoItem>>

    abstract fun onInfoItemClicked(infoItem: Any)
}