package com.lelloman.launcher.packages

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import com.lelloman.common.di.qualifiers.IoScheduler
import com.lelloman.common.view.BroadcastReceiverWrap
import com.lelloman.common.view.ResourceProvider
import com.lelloman.launcher.R
import com.lelloman.launcher.classification.ClassifiedPackage
import com.lelloman.launcher.logger.LauncherLoggerFactory
import com.lelloman.launcher.persistence.db.ClassifiedIdentifierDao
import com.lelloman.launcher.persistence.db.model.ClassifiedIdentifier
import com.lelloman.launcher.persistence.db.model.PackageLaunch
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject

class PackagesManager(
    @IoScheduler private val ioScheduler: Scheduler,
    private val packageManager: PackageManager,
    loggerFactory: LauncherLoggerFactory,
    broadcastReceiverWrap: BroadcastReceiverWrap,
    private val launchesPackage: Package,
    classifiedPackageIdentifierDao: ClassifiedIdentifierDao,
    private val mainPackage: Package,
    private val resourceProvider: ResourceProvider,
    val queryActivityIntent: Intent = Intent(Intent.ACTION_MAIN, null)
        .addCategory(Intent.CATEGORY_LAUNCHER)
) {

    private val logger = loggerFactory.getLogger(PackagesManager::class.java)
    private val installedPackagesSubject = BehaviorSubject.create<List<Package>>()
    private val updatingPackagesSubject = BehaviorSubject.create<Boolean>().apply {
        onNext(false)
    }

    val installedPackages: Observable<List<Package>> = installedPackagesSubject.hide()
    val updatingPackages: Observable<Boolean> = updatingPackagesSubject.hide()

    val classifiedPackages: Observable<List<Package>> = Observable
        .combineLatest(
            classifiedPackageIdentifierDao.getAll().toObservable(),
            installedPackages,
            BiFunction<List<ClassifiedIdentifier>, List<Package>, List<ClassifiedPackage>> { classified, installed ->
                val classificationMap = classified.map { it.identifier to it.score }.toMap()
                installed.map {
                    val identifier = it.identifier
                    val hasScore = classificationMap.containsKey(identifier)
                    ClassifiedPackage(
                        pkg = it,
                        score = if (hasScore) classificationMap[identifier]!! else 0.0
                    )
                }
            }
        )
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
            .doAfterTerminate { updatingPackagesSubject.onNext(false) }
            .subscribe({
                installedPackagesSubject.onNext(it)
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
