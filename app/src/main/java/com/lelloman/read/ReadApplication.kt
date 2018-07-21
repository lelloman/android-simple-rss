package com.lelloman.read

import android.app.Activity
import android.app.Application
import android.content.BroadcastReceiver
import com.lelloman.read.core.PicassoWrap
import com.lelloman.read.core.di.AppModule
import com.lelloman.read.core.di.DaggerAppComponent
import com.lelloman.read.persistence.db.AppDatabase
import com.lelloman.read.persistence.db.SourcesDao
import com.lelloman.read.persistence.db.model.Source
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasBroadcastReceiverInjector
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


open class ReadApplication : Application(), HasActivityInjector, HasBroadcastReceiverInjector {

    @Inject
    lateinit var dispatchingActivityAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var dispatchingReceiverAndroidInjector: DispatchingAndroidInjector<BroadcastReceiver>

    @Inject
    lateinit var db: AppDatabase

    @Inject
    lateinit var sourcesDao: SourcesDao

    @Inject
    lateinit var picassoWrap: PicassoWrap

    override fun activityInjector() = dispatchingActivityAndroidInjector

    override fun broadcastReceiverInjector() = dispatchingReceiverAndroidInjector

    override fun onCreate() {
        super.onCreate()
        instance = this
        inject()

        if(BuildConfig.DEBUG){
            picassoWrap.enableImageSourceIndicator()
        }

        Completable
            .fromAction {
                db.clearAllTables()
                sourcesDao.insert(
                    Source(
                        id = 0L,
                        name = "repubblica",
                        url = "http://www.repubblica.it/rss/homepage/rss2.0.xml",
                        lastFetched = 0L,
                        isActive = true
                    )
                )
                sourcesDao.insert(
                    Source(
                        id = 0L,
                        name = "fanpage",
                        url = "https://www.fanpage.it/feed/",
                        lastFetched = 0L,
                        isActive = true
                    )
                )
                sourcesDao.insert(
                    Source(
                        id = 0L,
                        name = "ansa",
                        url = "http://www.ansa.it/sito/ansait_rss.xml",
                        lastFetched = 0L,
                        isActive = true
                    )
                )
            }
            .subscribeOn(Schedulers.io())
            .subscribe()

    }

    protected open fun inject() {
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
            .inject(this)
    }

    companion object {

        private lateinit var instance: ReadApplication

        fun getPicassoWrap() = instance.picassoWrap
    }
}