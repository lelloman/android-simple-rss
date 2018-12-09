package com.lelloman.pdfscores

import android.app.Activity
import android.content.ContentProvider
import android.util.Log
import com.lelloman.common.BaseApplication
import com.lelloman.common.di.BaseApplicationModule
import com.lelloman.pdfscores.di.DaggerAppComponent
import com.lelloman.pdfscores.publicapi.PublicPdfScoresAppsFinder
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasContentProviderInjector
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

open class PdfScoresApplication : BaseApplication(), HasActivityInjector, HasContentProviderInjector {

    @Inject
    lateinit var dispatchingActivityAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var dispatchingContentProviderAndroidInjector: DispatchingAndroidInjector<ContentProvider>

    @Inject
    lateinit var publicPdfScoresAppsFinder: PublicPdfScoresAppsFinder

    override fun inject() = DaggerAppComponent
        .builder()
        .baseApplicationModule(BaseApplicationModule(this))
        .build()
        .inject(this)

    override fun onCreate() {
        super.onCreate()
        val unused = publicPdfScoresAppsFinder
            .pdfScoresApps
            .map { apps ->
                apps.map {packageName ->
                    publicPdfScoresAppsFinder.getAssetCollectionFileUri(packageName)
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { apps ->
                Log.d("ASDASD", "found ${apps.size} apps")
                apps.forEach {
                    Log.d("ASDASD", "pdfscore app -> $it")
                }
            }
    }

    override fun activityInjector() = dispatchingActivityAndroidInjector

    override fun contentProviderInjector() = dispatchingContentProviderAndroidInjector
}