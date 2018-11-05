package com.lelloman.launcher.ui.main.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.lelloman.common.navigation.PackageIntentNavigationEvent
import com.lelloman.common.utils.LazyLiveData
import com.lelloman.common.utils.TimeProvider
import com.lelloman.common.utils.model.Resolution
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.launcher.logger.LauncherLoggerFactory
import com.lelloman.launcher.logger.ShouldLogToFile
import com.lelloman.launcher.packages.Package
import com.lelloman.launcher.packages.PackagesManager
import com.lelloman.launcher.persistence.db.PackageLaunchDao
import com.lelloman.launcher.persistence.db.model.PackageLaunch
import com.lelloman.launcher.ui.main.AppsDrawerListItem
import com.lelloman.launcher.ui.main.HomePage
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject

class MainViewModelImpl(
    private val ioScheduler: Scheduler,
    private val timeProvider: TimeProvider,
    private val launcherLoggerFactoryFactory: LauncherLoggerFactory,
    private val packagesManager: PackagesManager,
    dependencies: BaseViewModel.Dependencies,
    private val packageLaunchDao: PackageLaunchDao
) : MainViewModel(dependencies), ShouldLogToFile {

    private val logger = launcherLoggerFactoryFactory.getLogger(javaClass)

    private val searchQuerySubject = BehaviorSubject.create<String>().apply {
        onNext("")
    }

    override val drawerApps: MutableLiveData<List<AppsDrawerListItem>> by LazyLiveData {
        subscription {
            Observable
                .combineLatest(
                    packagesManager
                        .classifiedPackages//.installedPackages
                        .map {
                            val out = mutableListOf<AppsDrawerListItem>()
                            out.add(SearchDrawerListItem)
                            out.addAll(it.map(::PackageDrawerListItem))
                            logger.d("spitting out ${it.size} classifiedPackages")
                            out
                        },
                    searchQuerySubject
                        .hide(),
                    BiFunction<List<AppsDrawerListItem>, String, List<AppsDrawerListItem>> { items, query ->
                        logger.d("bifunctioning ${items.size} packages and query \"$query\"")
                        items.filter { !it.isFilteredOutBy(query) }
                    }
                )
                .doOnSubscribe { logger.d("drawerApps subscribed") }
                .subscribeOn(ioScheduler)
                .subscribe {
                    logger.d("posting ${it.size} AppsDrawerListItem")
                    drawerApps.postValue(it)
                }
        }
    }

    override val classifiedApps = MutableLiveData<List<PackageDrawerListItem>>().apply {
        //        subscription {
//            packagesManager
//                .classifiedPackages
//                .map {
//                    it.map(::PackageDrawerListItem)
//                }
//                .subscribeOn(ioScheduler)
//                .subscribe {
//                    postValue(it)
//                }
//        }
    }

    private val pageResolution = Resolution(5, 5)
    private val homePagesInternal = mutableListOf(
        object : HomePage {
            override val resolution = pageResolution
        },
        object : HomePage {
            override val resolution = pageResolution
        }
    )

    override val homePages: MutableLiveData<List<HomePage>> by LazyLiveData {
        homePages.postValue(homePagesInternal)
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