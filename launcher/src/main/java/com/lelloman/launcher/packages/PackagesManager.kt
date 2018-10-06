package com.lelloman.launcher.packages

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import com.lelloman.common.di.qualifiers.IoScheduler
import com.lelloman.common.logger.LoggerFactory
import com.lelloman.common.view.BroadcastReceiverWrap
import com.lelloman.launcher.classification.ClassifiedPackage
import com.lelloman.launcher.classification.PackageClassifier
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

class PackagesManager(
    @IoScheduler private val ioScheduler: Scheduler,
    private val packageManager: PackageManager,
    packageClassifier: PackageClassifier,
    loggerFactory: LoggerFactory,
    broadcastReceiverWrap: BroadcastReceiverWrap,
    private val launchesPackage: Package,
    val queryActivityIntent: Intent = Intent(Intent.ACTION_MAIN, null)
        .addCategory(Intent.CATEGORY_LAUNCHER)
) {

    private val logger = loggerFactory.getLogger(PackagesManager::class.java.simpleName)
    private val installedPackagesSubject = BehaviorSubject.create<List<Package>>()

    val installedPackages: Observable<List<Package>> = installedPackagesSubject.hide()

    val classifiedPackages: Observable<List<Package>> = installedPackagesSubject
        .flatMapSingle(packageClassifier::classify)
        .map { classifiedPackages ->
            classifiedPackages
                .asSequence()
                .sortedByDescending { it.score }
                .map(ClassifiedPackage::pkg)
                .toList()
        }

    init {
        @Suppress("UNUSED_VARIABLE")
        val unused = broadcastReceiverWrap
            .broadcasts
            .subscribeOn(ioScheduler)
            .observeOn(ioScheduler)
            .subscribe { intent ->
                when (intent.action) {
                    Intent.ACTION_PACKAGE_ADDED,
                    Intent.ACTION_PACKAGE_CHANGED,
                    Intent.ACTION_PACKAGE_REMOVED,
                    Intent.ACTION_PACKAGE_REPLACED -> updateInstalledPackages()
                }
            }
        broadcastReceiverWrap.register(
            dataScheme = "package",
            actions = arrayOf(
                Intent.ACTION_PACKAGE_ADDED,
                Intent.ACTION_PACKAGE_CHANGED,
                Intent.ACTION_PACKAGE_REMOVED,
                Intent.ACTION_PACKAGE_REPLACED
            )
        )
        updateInstalledPackages()
    }

    @SuppressLint("CheckResult")
    private fun updateInstalledPackages() {
        Single
            .fromCallable {
                getPackagesFromPackageManager().apply {
                    add(launchesPackage)
                    sortBy { it.label.toString() }
                }
            }
            .subscribeOn(ioScheduler)
            .observeOn(ioScheduler)
            .subscribe({
                installedPackagesSubject.onNext(it)
            }, {
                logger.e("Error while querying packages", it)
                throw it
            })
    }

    private fun getPackagesFromPackageManager() =
        packageManager
            .queryIntentActivities(queryActivityIntent, 0)
            .asSequence()
            .mapIndexed { index, resolveInfo ->
                Package(
                    id = index.toLong(),
                    label = resolveInfo.loadLabel(packageManager),
                    packageName = resolveInfo.activityInfo.packageName,
                    activityName = resolveInfo.activityInfo.name,
                    drawable = resolveInfo.loadIcon(packageManager)
                )
            }
            .toMutableList()
}
