package com.lelloman.pdfscores

import android.app.Activity
import android.app.Service
import android.util.Log
import com.lelloman.common.BaseApplication
import com.lelloman.common.di.BaseApplicationModule
import com.lelloman.pdfscores.di.DaggerAppComponent
import com.lelloman.pdfscores.persistence.db.AuthorsDao
import com.lelloman.pdfscores.persistence.db.PdfScoresDao
import com.lelloman.pdfscores.publicapi.PublicPdfScoresAppsFinder
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasServiceInjector
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

open class PdfScoresApplication : BaseApplication(), HasActivityInjector, HasServiceInjector {

    @Inject
    lateinit var dispatchingActivityAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var dispatchingServiceAndroidInjector: DispatchingAndroidInjector<Service>

    @Inject
    lateinit var pdfScoresDao: PdfScoresDao

    @Inject
    lateinit var authorsDao: AuthorsDao

    @Inject
    lateinit var appsFinder: PublicPdfScoresAppsFinder

    override fun inject() = DaggerAppComponent
        .builder()
        .baseApplicationModule(BaseApplicationModule(this))
        .build()
        .inject(this)

    override fun activityInjector() = dispatchingActivityAndroidInjector

    override fun serviceInjector() = dispatchingServiceAndroidInjector

    override fun onCreate() {
        super.onCreate()

        val unused = appsFinder
            .pdfScoresApps
            .subscribeOn(Schedulers.io())
            .subscribe {
                it.forEach {
                    Log.d("ASDASD", "${it.packageName} ${it.publicApiVersion}")
                }
            }

        Completable
            .fromAction(authorsDao::deleteAll)
            .subscribeOn(Schedulers.io())
            .blockingAwait()
    }
}