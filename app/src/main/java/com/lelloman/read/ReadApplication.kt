package com.lelloman.read

import android.app.Activity
import android.app.Application
import android.content.BroadcastReceiver
import com.lelloman.read.core.FaviconBitmapProvider
import com.lelloman.read.core.PicassoWrap
import com.lelloman.read.core.di.AppModule
import com.lelloman.read.core.di.DaggerAppComponent
import com.lelloman.read.core.logger.Logger
import com.lelloman.read.core.logger.LoggerFactory
import com.lelloman.read.http.HttpClientException
import com.lelloman.read.persistence.db.AppDatabase
import com.lelloman.read.persistence.db.SourcesDao
import com.lelloman.read.persistence.db.model.Source
import com.lelloman.read.persistence.settings.AppSettings
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasBroadcastReceiverInjector
import io.reactivex.Completable
import io.reactivex.plugins.RxJavaPlugins
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

    @Inject
    lateinit var faviconBitmapProvider: FaviconBitmapProvider

    @Inject
    lateinit var appSettings: AppSettings

    @Inject
    lateinit var loggerFactory: LoggerFactory

    private lateinit var logger: Logger

    override fun activityInjector() = dispatchingActivityAndroidInjector

    override fun broadcastReceiverInjector() = dispatchingReceiverAndroidInjector

    override fun onCreate() {
        super.onCreate()
        instance = this
        inject()
        logger = loggerFactory.getLogger(javaClass.simpleName)

        if (BuildConfig.DEBUG) {
            picassoWrap.enableImageSourceIndicator()
        }
        RxJavaPlugins.setErrorHandler {
            val isHttpClientException = it is HttpClientException || it.cause is HttpClientException || it.cause?.cause is HttpClientException
            val httpClientExtraMsg = if (isHttpClientException) {
                ". This might be an un-subscribed http call, in which case it should be fine."
            } else {
                ""
            }
            logger.e("RxJavaPlugin error handler$httpClientExtraMsg", it)
        }

        appSettings.setShouldShowWalkthtough(true)

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
                sourcesDao.insert(
                    Source(
                        id = 0L,
                        name = "ilmattino",
                        url = "http://www.ilmattino.it/rss.php",
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

        fun getFaviconBitmapProvider() = instance.faviconBitmapProvider
    }
}