package com.lelloman.launcher.ui.main.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.launcher.packages.Package
import com.lelloman.launcher.ui.main.AppsDrawerListItem
import com.lelloman.launcher.ui.main.HomePage

abstract class MainViewModel(
    dependencies: Dependencies
) : BaseViewModel(dependencies) {

    abstract val drawerApps: MutableLiveData<List<AppsDrawerListItem>>
    abstract val classifiedApps: MutableLiveData<List<PackageDrawerListItem>>

    abstract val homePages: MutableLiveData<List<HomePage>>

    abstract fun onPackageClicked(pkg: Package)
    abstract fun onSearchQueryChanged(searchQuery: String)
}