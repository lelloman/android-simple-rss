package com.lelloman.launcher

import android.arch.lifecycle.MutableLiveData
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.launcher.packages.Package

abstract class MainViewModel(
    dependencies: Dependencies
) : BaseViewModel(dependencies) {

    abstract val packages: MutableLiveData<List<Package>>
}