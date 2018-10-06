package com.lelloman.launcher.ui.launches.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.lelloman.common.utils.LazyLiveData
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.launcher.persistence.PackageLaunchDao
import com.lelloman.launcher.packages.PackagesManager
import com.lelloman.launcher.ui.launches.PackageLaunchListItem
import io.reactivex.Scheduler

class LaunchesViewModelImpl(
    ioScheduler: Scheduler,
    dependencies: BaseViewModel.Dependencies,
    packageLaunchDao: PackageLaunchDao,
    packagesManager: PackagesManager
) : LaunchesViewModel(dependencies) {

    override val launches:MutableLiveData<List<PackageLaunchListItem>> by LazyLiveData {
        subscription {
            packageLaunchDao
                .getAll()
                .map { launches ->
                    launches.map {
                        PackageLaunchListItem(
                            packageLaunch = it,
                            icon = packagesManager.getIconForPackageLaunch(it)
                        )
                    }
                }
                .subscribeOn(ioScheduler)
                .observeOn(ioScheduler)
                .subscribe {
                    launches.postValue(it)
                }
        }
    }

}