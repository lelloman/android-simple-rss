package com.lelloman.simplerss

import android.app.Activity
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import com.lelloman.common.di.BaseApplicationModule
import com.lelloman.common.logger.Logger
import com.lelloman.common.logger.LoggerFactory
import com.lelloman.common.view.PicassoWrap
import com.lelloman.simplerss.di.DaggerAppComponent
import com.lelloman.simplerss.feed.FaviconBitmapProvider
import com.lelloman.simplerss.http.HttpClientException
import com.lelloman.simplerss.persistence.db.AppDatabase
import com.lelloman.simplerss.persistence.db.SourcesDao
import com.lelloman.simplerss.persistence.settings.AppSettings
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasBroadcastReceiverInjector
import io.reactivex.plugins.RxJavaPlugins
import java.io.InterruptedIOException
import javax.inject.Inject

open class SimpleRssApplication : Application(), HasActivityInjector, HasBroadcastReceiverInjector {

    @Inject
    lateinit var dispatchingActivityAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var dispatchingReceiverAndroidInjector: DispatchingAndroidInjector<BroadcastReceiver>

    @Inject
    lateinit var db: AppDatabase

    @Inject
    lateinit var sourcesDao: SourcesDao

    @Inject
    open lateinit var picassoWrap: PicassoWrap

    @Inject
    lateinit var faviconBitmapProvider: FaviconBitmapProvider

    @Inject
    lateinit var appSettings: AppSettings

    @Inject
    lateinit var loggerFactory: LoggerFactory

    private lateinit var logger: Logger

    override fun activityInjector() = dispatchingActivityAndroidInjector

    override fun broadcastReceiverInjector() = dispatchingReceiverAndroidInjector

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        inject()
    }

    private fun Throwable?.isHttpClientException(): Boolean =
        this is HttpClientException || this?.cause is HttpClientException

    private fun Throwable?.isInterruptedIoException(): Boolean =
        this is InterruptedIOException || this?.cause is InterruptedIOException

    override fun onCreate() {
        super.onCreate()
        SimpleRssApplication.instance = this

        logger = loggerFactory.getLogger(javaClass)

        if (BuildConfig.DEBUG) {
            picassoWrap.enableImageSourceIndicator()
        }
        RxJavaPlugins.setErrorHandler {
            when {
                it.isInterruptedIoException() && it.isHttpClientException() -> {
                    logger.d("HttpClient/InterruptedIOException occurred", it)
                }
                else -> logger.e("RxJavaPlugin error handler", it)
            }
        }
    }

    open fun inject() = DaggerAppComponent
        .builder()
        .baseApplicationModule(BaseApplicationModule(this))
        .build()
        .inject(this)

    companion object {

        private lateinit var instance: SimpleRssApplication

        fun getPicassoWrap() = SimpleRssApplication.instance.picassoWrap

        fun getFaviconBitmapProvider() = SimpleRssApplication.instance.faviconBitmapProvider
    }
}