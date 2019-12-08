package com.lelloman.simplerss

import com.lelloman.common.BaseApplication
import com.lelloman.common.http.HttpClientException
import com.lelloman.common.http.HttpModuleFactory
import com.lelloman.common.logger.Logger
import com.lelloman.common.logger.LoggerFactory
import com.lelloman.common.view.PicassoWrap
import com.lelloman.simplerss.di.*
import com.lelloman.simplerss.feed.FaviconBitmapProvider
import com.lelloman.simplerss.persistence.settings.AppSettings
import io.reactivex.plugins.RxJavaPlugins
import org.koin.android.ext.android.inject
import java.io.InterruptedIOException

open class SimpleRssApplication : BaseApplication() {

    protected open val picassoWrap by inject<PicassoWrap>()

    protected val faviconBitmapProvider by inject<FaviconBitmapProvider>()

    protected val appSettings by inject<AppSettings>()

    protected val loggerFactory by inject<LoggerFactory>()

    private lateinit var logger: Logger

    private fun Throwable?.isHttpClientException(): Boolean =
        this is HttpClientException || this?.cause is HttpClientException

    private fun Throwable?.isInterruptedIoException(): Boolean =
        this is InterruptedIOException || this?.cause is InterruptedIOException

    override fun onCreate() {
        super.onCreate()
        instance = this

        logger = loggerFactory.getLogger(javaClass)

        if (BuildConfig.DEBUG) {
//            picassoWrap.enableImageSourceIndicator()
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

    override fun getKoinModuleFactories() = mutableListOf(
        DbModuleFactory(),
        FeedModuleFactory(),
        HtmlModuleFactory(),
        HttpModuleFactory(),
        RepositoryModuleFactory(),
        SettingsModuleFactory(),
        ViewModelModuleFactory()
    )

    companion object {

        lateinit var instance: SimpleRssApplication

        fun getPicassoWrap() = instance.picassoWrap

        fun getFaviconBitmapProvider() = instance.faviconBitmapProvider
    }
}