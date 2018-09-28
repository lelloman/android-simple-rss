package com.lelloman.launcher

import android.arch.lifecycle.MutableLiveData
import com.lelloman.common.di.qualifiers.IoScheduler
import com.lelloman.common.utils.LazyLiveData
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.launcher.packages.Package
import com.lelloman.launcher.packages.PackagesManager
import io.reactivex.Scheduler

class MainViewModelImpl(
    @IoScheduler ioScheduler: Scheduler,
    private val packagesManager: PackagesManager,
    dependencies: BaseViewModel.Dependencies
) : MainViewModel(dependencies) {

    override val packages: MutableLiveData<List<Package>> by LazyLiveData {
        subscription {
            packagesManager
                .installedPackages
                .subscribeOn(ioScheduler)
                .subscribe(packages::postValue)
        }
    }
}