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
    lateinit var db: com.lelloman.simplerss.persistence.db.AppDatabase

    @Inject
    lateinit var sourcesDao: com.lelloman.simplerss.persistence.db.SourcesDao

    @Inject
    open lateinit var picassoWrap: PicassoWrap

    @Inject
    lateinit var faviconBitmapProvider: com.lelloman.simplerss.feed.FaviconBitmapProvider

    @Inject
    lateinit var appSettings: com.lelloman.simplerss.persistence.settings.AppSettings

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
        this is com.lelloman.simplerss.http.HttpClientException || this?.cause.isHttpClientException()

    private fun Throwable?.isInterruptedIoException(): Boolean =
        this is InterruptedIOException || this?.cause.isInterruptedIoException()

    override fun onCreate() {
        super.onCreate()
        com.lelloman.simplerss.SimpleRssApplication.Companion.instance = this

        logger = loggerFactory.getLogger(javaClass)

        if (com.lelloman.simplerss.BuildConfig.DEBUG) {
            picassoWrap.enableImageSourceIndicator()
        }
        RxJavaPlugins.setErrorHandler {
            when {
                it.isInterruptedIoException() && it.isHttpClientException() -> {
                    // TODO use d(String, Throwable) when available from common
                    logger.w("HttpClient/InterruptedIOException occurred", it)
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

        private lateinit var instance: com.lelloman.simplerss.SimpleRssApplication

        fun getPicassoWrap() = com.lelloman.simplerss.SimpleRssApplication.Companion.instance.picassoWrap

        fun getFaviconBitmapProvider() = com.lelloman.simplerss.SimpleRssApplication.Companion.instance.faviconBitmapProvider
    }
}