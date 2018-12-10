package com.lelloman.pdfscores

import android.app.Activity
import android.content.ContentProvider
import android.util.Log
import com.lelloman.common.BaseApplication
import com.lelloman.common.di.BaseApplicationModule
import com.lelloman.common.view.AppTheme
import com.lelloman.pdfscores.di.DaggerAppComponent
import com.lelloman.pdfscores.publicapi.PublicPdfScoresAppsFinder
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasContentProviderInjector
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
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
        AppTheme.DEFAULT = AppTheme.fromName(BuildConfig.DEFAULT_THEME)
        val unused = publicPdfScoresAppsFinder
            .pdfScoresApps
            .map { apps ->
                apps.map { packageName ->
                    publicPdfScoresAppsFinder.getAssetCollectionFileUri(packageName)
                }
            }
            .doOnNext { apps ->
                Log.d("ASDASD", "found ${apps.size} apps")
                apps.forEach {
                    Log.d("ASDASD", "pdfscore app -> $it")
                }
            }
            .flatMap { uris ->
                Observable
                    .fromIterable(uris)
                    .map { uri ->
                        val dstFile = File(cacheDir, uri.hashCode().toString())
                        val assetFileDescriptor = publicPdfScoresAppsFinder.openAssetCollectionRootFile(uri)
                        assetFileDescriptor.createInputStream().copyTo(dstFile.outputStream())
                        dstFile.absolutePath
                    }
                    .onErrorReturn {
                        Log.e("ASDASD", "error opening asset file", it)
                        ""
                    }
                    .subscribeOn(Schedulers.newThread())
                    .toList()
                    .toObservable()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { assetRootFilePaths ->
                Log.d("ASDASD", "Received ${assetRootFilePaths.size} asset collection json file")
                assetRootFilePaths.forEach {
                    Log.d("ASDASD", "file path - > <$it>")
                    if(it.isNotEmpty()) {
                        Log.d("ASDASD", File(it).readText())
                    }
                }
            }
    }

    override fun activityInjector() = dispatchingActivityAndroidInjector

    override fun contentProviderInjector() = dispatchingContentProviderAndroidInjector
}