package com.lelloman.pdfscores.publicapi

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import com.lelloman.pdfscores.IPublicPdfScoresService
import com.lelloman.pdfscores.publicapi.PublicApiConstants.ACTION_PDF_SCORES_PROVIDER
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class PublicpdfScoresAppsFinderImpl(
    private val context: Context,
    private val packageManager: PackageManager,
    private val ioScheduler: Scheduler,
    private val newThreadScheduler: Scheduler
) : PublicPdfScoresAppsFinder {

    private val pdfScoresAppsSubject = PublishSubject.create<List<PublicPdfScoresApp>>()
    override val pdfScoresApps: Observable<List<PublicPdfScoresApp>> = pdfScoresAppsSubject.hide()

    init {
        queryInstalledApps()
    }

    private fun queryInstalledApps() = synchronized(this) {
        val resolveInfos = packageManager.queryIntentServices(Intent(ACTION_PDF_SCORES_PROVIDER), 0)
        val unused = Observable
            .fromIterable(resolveInfos)
            .flatMap {
                val intent = Intent(ACTION_PDF_SCORES_PROVIDER).setComponent(
                    ComponentName(it.serviceInfo.packageName, it.serviceInfo.name)
                )
                val connection = PublicPdfScoresServiceConnection()
                context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
                connection
                    .service
                    .timeout(3, TimeUnit.SECONDS)
                    .subscribeOn(newThreadScheduler)
                    .onErrorResumeNext { t: Throwable ->
                        Observable.empty<IPublicPdfScoresService>()
                    }
            }
            .map {
                object : PublicPdfScoresApp {
                    override val packageName: String = it.packagename
                    override val publicApiVersion = it.version
                }
            }
            .subscribeOn(ioScheduler)
            .toList()
            .subscribe({
                pdfScoresAppsSubject.onNext(it)
            }, {
                Log.e("ASDASD", "error", it)
            })
    }
}