package com.lelloman.read

import android.app.Activity
import android.app.Application
import com.lelloman.read.core.di.AppModule
import com.lelloman.read.core.di.DaggerAppComponent
import com.lelloman.read.persistence.AppDatabase
import com.lelloman.read.persistence.SourcesDao
import com.lelloman.read.persistence.model.Source
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


open class ReadApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var db: AppDatabase

    @Inject
    lateinit var sourcesDao: SourcesDao

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingAndroidInjector

    override fun onCreate() {
        super.onCreate()
        inject()

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
}