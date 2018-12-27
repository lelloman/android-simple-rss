package com.lelloman.pdfscores

import android.app.Activity
import android.content.ContentProvider
import android.content.Context
import com.lelloman.common.BaseApplication
import com.lelloman.common.di.BaseApplicationModule
import com.lelloman.common.di.qualifiers.IoScheduler
import com.lelloman.common.di.qualifiers.UiScheduler
import com.lelloman.common.view.AppTheme
import com.lelloman.pdfscores.di.DaggerAppComponent
import com.lelloman.pdfscores.persistence.assets.AssetsCollectionInserter
import com.lelloman.pdfscores.persistence.assets.AssetsCollectionProvider
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasContentProviderInjector
import io.reactivex.Scheduler
import javax.inject.Inject

open class PdfScoresApplication : BaseApplication(), HasActivityInjector, HasContentProviderInjector {

    @Inject
    lateinit var dispatchingActivityAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var dispatchingContentProviderAndroidInjector: DispatchingAndroidInjector<ContentProvider>

    @Inject
    lateinit var assetsCollectionInserter: AssetsCollectionInserter

    @Inject
    @field:IoScheduler
    lateinit var ioScheduler: Scheduler

    @Inject
    @field:UiScheduler
    lateinit var uiScheduler: Scheduler

    override fun inject() = DaggerAppComponent
        .builder()
        .baseApplicationModule(BaseApplicationModule(this))
        .build()
        .inject(this)

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        AppTheme.DEFAULT = AppTheme.fromName(BuildConfig.DEFAULT_THEME)
    }

    override fun onCreate() {
        super.onCreate()
        assetsCollectionInserter
            .insertAssetsCollectionIntoDb()
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe()
    }

    override fun activityInjector() = dispatchingActivityAndroidInjector

    override fun contentProviderInjector() = dispatchingContentProviderAndroidInjector
}