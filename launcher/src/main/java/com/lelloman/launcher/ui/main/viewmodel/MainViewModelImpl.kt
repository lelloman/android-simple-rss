package com.lelloman.launcher.ui.main.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.lelloman.common.di.qualifiers.IoScheduler
import com.lelloman.common.navigation.PackageIntentNavigationEvent
import com.lelloman.common.utils.TimeProvider
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.launcher.packages.Package
import com.lelloman.launcher.persistence.PackageLaunchDao
import com.lelloman.launcher.packages.PackagesManager
import com.lelloman.launcher.persistence.model.PackageLaunch
import com.lelloman.launcher.ui.main.AppsDrawerListItem
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject

class MainViewModelImpl(
    @IoScheduler private val ioScheduler: Scheduler,
    private val packagesManager: PackagesManager,
    dependencies: BaseViewModel.Dependencies,
    private val packageLaunchDao: PackageLaunchDao,
    private val timeProvider: TimeProvider
) : MainViewModel(dependencies) {

    private val searchQuerySubject = BehaviorSubject.create<String>(). apply {
        onNext("")
    }

    override val drawerApps = MutableLiveData<List<AppsDrawerListItem>>().apply {
        subscription {
            Observable
                .combineLatest(
                    packagesManager
                        .installedPackages
                        .map {
                            val out = mutableListOf<AppsDrawerListItem>()
                            out.add(SearchDrawerListItem)
                            out.addAll(it.map(::PackageDrawerListItem))
                            out
                        },
                    searchQuerySubject.hide(),
                    BiFunction<List<AppsDrawerListItem>, String, List<AppsDrawerListItem>> { items, query ->
                        items.filter { !it.isFilteredOutBy(query) }
                    }
                )
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

    override fun onSearchQueryChanged(searchQuery: String) {
        searchQuerySubject.onNext(searchQuery)
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