package com.lelloman.launcher.ui.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.databinding.Observable
import android.databinding.ObservableField
import android.util.Log
import com.lelloman.common.di.qualifiers.IoScheduler
import com.lelloman.common.navigation.PackageIntentNavigationEvent
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.launcher.packages.Package
import com.lelloman.launcher.packages.PackagesManager
import com.lelloman.launcher.ui.AppsDrawerListItem
import io.reactivex.Scheduler

class MainViewModelImpl(
    @IoScheduler ioScheduler: Scheduler,
    private val packagesManager: PackagesManager,
    dependencies: BaseViewModel.Dependencies
) : MainViewModel(dependencies) {

    override val packages = MutableLiveData<List<AppsDrawerListItem>>().apply {
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

    init {
    }

    override fun onPackageClicked(pkg: Package) =
        navigate(PackageIntentNavigationEvent(pkg.packageName))
}