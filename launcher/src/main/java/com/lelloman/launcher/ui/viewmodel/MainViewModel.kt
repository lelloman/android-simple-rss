package com.lelloman.launcher.ui.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.launcher.packages.Package
import com.lelloman.launcher.ui.AppsDrawerListItem

abstract class MainViewModel(
    dependencies: Dependencies
) : BaseViewModel(dependencies) {

    abstract val packages: MutableLiveData<List<AppsDrawerListItem>>

    abstract fun onPackageClicked(pkg: Package)
}