package com.lelloman.launcher.packages

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import com.lelloman.common.di.qualifiers.IoScheduler
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

class PackagesManager(
    @IoScheduler private val ioScheduler: Scheduler,
    private val packageManager: PackageManager,
    context: Context
) {

    private val installedPackagesSubject = BehaviorSubject.create<List<Package>>()

    val installedPackages: Observable<List<Package>> = installedPackagesSubject.hide()

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                Intent.ACTION_PACKAGE_ADDED,
                Intent.ACTION_PACKAGE_CHANGED,
                Intent.ACTION_PACKAGE_REMOVED,
                Intent.ACTION_PACKAGE_REPLACED -> updateInstalledPackages()
            }
        }
    }

    init {
        context.registerReceiver(broadcastReceiver, IntentFilter().apply {
            addAction(Intent.ACTION_PACKAGE_ADDED)
            addAction(Intent.ACTION_PACKAGE_CHANGED)
            addAction(Intent.ACTION_PACKAGE_REMOVED)
            addAction(Intent.ACTION_PACKAGE_REPLACED)
        })
        updateInstalledPackages()
    }

    private fun updateInstalledPackages() {
        getPackagesFromPackageManager()
            .subscribeOn(ioScheduler)
            .observeOn(ioScheduler)
            .toObservable()
            .subscribe(installedPackagesSubject)
    }

    private fun getPackagesFromPackageManager(): Single<List<Package>> = Single.fromCallable {
        Intent(Intent.ACTION_MAIN, null)
            .addCategory(Intent.CATEGORY_LAUNCHER)
            .let { intent ->
                packageManager
                    .queryIntentActivities(intent, 0)
                    .asSequence()
                    .mapIndexed { index, resolveInfo ->
                        Package(
                            id = index.toLong(),
                            label = resolveInfo.loadLabel(packageManager),
                            packageName = resolveInfo.activityInfo.packageName,
                            drawable = resolveInfo.loadIcon(packageManager)
                        )
                    }
                    .sortedBy { it.label.toString().toLowerCase() }
                    .toList()
            }
    }
}