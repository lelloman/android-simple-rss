package com.lelloman.launcher.ui.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.lelloman.common.di.qualifiers.IoScheduler
import com.lelloman.common.navigation.PackageIntentNavigationEvent
import com.lelloman.common.utils.TimeProvider
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.launcher.packages.Package
import com.lelloman.launcher.packages.PackageLaunchDao
import com.lelloman.launcher.packages.PackagesManager
import com.lelloman.launcher.persistence.model.PackageLaunch
import com.lelloman.launcher.ui.AppsDrawerListItem
import io.reactivex.Completable
import io.reactivex.Scheduler

class MainViewModelImpl(
    @IoScheduler private val ioScheduler: Scheduler,
    private val packagesManager: PackagesManager,
    dependencies: BaseViewModel.Dependencies,
    private val packageLaunchDao: PackageLaunchDao,
    private val timeProvider: TimeProvider
) : MainViewModel(dependencies) {

    override val drawerApps = MutableLiveData<List<AppsDrawerListItem>>().apply {
        subscription {
            packagesManager
                .installedPackages
                .map {
                    val out = mutableListOf<AppsDrawerListItem>()
                    out.add(SearchDrawerListItem)
                    out.addAll(it.map(::PackageDrawerListItem))
                    out
                }
                .subscribeOn(ioScheduler)
                .subscribe {
                    postValue(it)
                }
        }
    }

    override val classifiedApps = MutableLiveData<List<PackageDrawerListItem>>().apply {
        subscription {
            packagesManager
                .classifiedPackages
                .map {
                    it.map(::PackageDrawerListItem)
                }
                .subscribeOn(ioScheduler)
                .subscribe {
                    postValue(it)
                }
        }
    }

    override fun onPackageClicked(pkg: Package) {
        navigate(PackageIntentNavigationEvent(
            packageName = pkg.packageName,
            activityName = pkg.activityName
        ))
        insertPackageLaunch(pkg)
    }

    private fun insertPackageLaunch(pkg: Package) = Completable
        .fromAction {
            packageLaunchDao.insert(PackageLaunch(
                timestampUtc = timeProvider.nowUtcMs(),
                packageName = pkg.packageName,
                activityName = pkg.activityName
            ))
        }
        .subscribeOn(ioScheduler)
        .subscribe()
}