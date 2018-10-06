package com.lelloman.launcher.ui.launches.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.launcher.ui.launches.PackageLaunchListItem

abstract class LaunchesViewModel(
    dependencies: Dependencies
) : BaseViewModel(dependencies) {

    abstract val launches: MutableLiveData<List<PackageLaunchListItem>>

    abstract fun onExportClicked()
    abstract fun onImportClicked()
}