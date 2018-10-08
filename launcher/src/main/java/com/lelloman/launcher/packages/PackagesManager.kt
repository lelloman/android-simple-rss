package com.lelloman.launcher.packages

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import com.lelloman.common.di.qualifiers.IoScheduler
import com.lelloman.common.logger.LoggerFactory
import com.lelloman.common.view.BroadcastReceiverWrap
import com.lelloman.common.view.ResourceProvider
import com.lelloman.launcher.R
import com.lelloman.launcher.classification.ClassifiedPackage
import com.lelloman.launcher.classification.PackageClassifier
import com.lelloman.launcher.persistence.model.PackageLaunch
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

class PackagesManager(
    @IoScheduler private val ioScheduler: Scheduler,
    private val packageManager: PackageManager,
    private val packageClassifier: PackageClassifier,
    loggerFactory: LoggerFactory,
    broadcastReceiverWrap: BroadcastReceiverWrap,
    private val launchesPackage: Package,
    private val mainPackage: Package,
    private val resourceProvider: ResourceProvider,
    val queryActivityIntent: Intent = Intent(Intent.ACTION_MAIN, null)
        .addCategory(Intent.CATEGORY_LAUNCHER)
) {

    private val logger = loggerFactory.getLogger(PackagesManager::class.java.simpleName)
    private val installedPackagesSubject = BehaviorSubject.create<List<Package>>()
    private val classifiedPackagesSubject = BehaviorSubject.create<List<Package>>()
    private val updatingPackagesSubject = BehaviorSubject.create<Boolean>().apply {
        onNext(false)
    }

    val installedPackages: Observable<List<Package>> = installedPackagesSubject.hide()
    val updatingPackages: Observable<Boolean> = updatingPackagesSubject.hide()

    val classifiedPackages: Observable<List<Package>> = classifiedPackagesSubject.hide()

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
    fun updateInstalledPackages() {
        if (updatingPackagesSubject.blockingFirst()) return
        updatingPackagesSubject.onNext(true)
        Single
            .fromCallable {
                getPackagesFromPackageManager().apply {
                    add(launchesPackage)
                    removeAll {
                        it.packageName == mainPackage.packageName && it.activityName == mainPackage.activityName
                    }
                    sortBy { it.label.toString() }
                }
            }
            .subscribeOn(ioScheduler)
            .observeOn(ioScheduler)
            .doOnSuccess(installedPackagesSubject::onNext)
            .flatMap { packageClassifier.classify(it) }
            .map { classifiedPackages ->
                classifiedPackages
                    .asSequence()
                    .sortedByDescending { it.score }
                    .map(ClassifiedPackage::pkg)
                    .toList()
            }
            .doAfterTerminate { updatingPackagesSubject.onNext(false) }
            .subscribe({
                classifiedPackagesSubject.onNext(it)
            }, {
                logger.e("Error while querying packages", it)
                throw it
            })
    }

    fun getIconForPackageLaunch(packageLaunch: PackageLaunch): Drawable = try {
        val resolveInfo = ResolveInfo()
        resolveInfo.activityInfo = ActivityInfo()
        resolveInfo.activityInfo.packageName = packageLaunch.packageName
        resolveInfo.activityInfo.name = packageLaunch.activityName
        val applicationInfo = packageManager.getApplicationInfo(packageLaunch.packageName, 0)
        resolveInfo.activityInfo.applicationInfo = applicationInfo
        resolveInfo.loadIcon(packageManager)
    } catch (exception: Exception) {
        logger.w("Error while trying to get icon for PackageLaunch", exception)
        resourceProvider.getDrawable(R.mipmap.ic_launcher)
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
