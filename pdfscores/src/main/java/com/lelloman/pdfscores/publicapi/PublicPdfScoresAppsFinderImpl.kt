package com.lelloman.pdfscores.publicapi

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetFileDescriptor
import android.net.Uri
import android.util.Log
import com.lelloman.common.di.qualifiers.ApplicationPackageName
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.subjects.BehaviorSubject

class PublicPdfScoresAppsFinderImpl(
    private val context: Context,
    private val packageManager: PackageManager,
    private val ioScheduler: Scheduler,
    private val newThreadScheduler: Scheduler,
    @ApplicationPackageName applicationPackageName: String
) : PublicPdfScoresAppsFinder {

    private val pdfScoresAppsSubject = BehaviorSubject.create<List<String>>()
    override val pdfScoresApps: Observable<List<String>> = pdfScoresAppsSubject.hide()

    private val nonVariantApplicationPackageName = applicationPackageName
        .split(".")
        .dropLast(1)
        .joinToString(".")

    init {
        queryInstalledApps()
    }

    override fun getAssetCollectionFileUri(packageName: String) = "content://$packageName.provider/asset/collection"

    override fun openAssetCollectionRootFile(uri: String) = context
        .contentResolver
        .openAssetFileDescriptor(Uri.parse(uri), "r")
        ?: TODO("some fancy exception")

    private fun queryInstalledApps() = synchronized(this) {
        val resolveInfos = packageManager.getInstalledApplications(0)
        val unused = Observable
            .fromIterable(resolveInfos)
            .filter { it.packageName.startsWith(nonVariantApplicationPackageName) }
            .map { it.packageName }
            .subscribeOn(ioScheduler)
            .toList()
            .subscribe({
                pdfScoresAppsSubject.onNext(it)
            }, {
                Log.e("ASDASD", "error", it)
            })
    }
}