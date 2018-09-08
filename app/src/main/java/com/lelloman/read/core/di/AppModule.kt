package com.lelloman.read.core.di

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.content.Context
import com.lelloman.read.core.ActionTokenProvider
import com.lelloman.read.core.FaviconBitmapProvider
import com.lelloman.read.core.MeteredConnectionChecker
import com.lelloman.read.core.MeteredConnectionCheckerImpl
import com.lelloman.read.core.PicassoWrap
import com.lelloman.read.core.PicassoWrapImpl
import com.lelloman.read.core.ResourceProvider
import com.lelloman.read.core.ResourceProviderImpl
import com.lelloman.read.core.SemanticTimeProvider
import com.lelloman.read.core.TimeProvider
import com.lelloman.read.core.di.qualifiers.IoScheduler
import com.lelloman.read.core.di.qualifiers.NewThreadScheduler
import com.lelloman.read.core.di.qualifiers.UiScheduler
import com.lelloman.read.core.logger.LoggerFactory
import com.lelloman.read.core.logger.LoggerFactoryImpl
import com.lelloman.read.core.navigation.NavigationRouter
import com.lelloman.read.persistence.settings.AppSettings
import com.lelloman.read.utils.UrlValidator
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Provider
import javax.inject.Singleton

@Module
open class AppModule(private val application: Application) {

    @Provides
    fun provideContext(): Context = application

    @Singleton
    @Provides
    @IoScheduler
    fun provideIoScheduler(): Scheduler = Schedulers.io()

    @Singleton
    @Provides
    @UiScheduler
    fun provideUiScheduler(): Scheduler = AndroidSchedulers.mainThread()

    @Singleton
    @Provides
    @NewThreadScheduler
    fun provideNewThreadScheduler(): Scheduler = Schedulers.newThread()

    @Provides
    fun provideNavigationRouter() = NavigationRouter()

    @Singleton
    @Provides
    fun provideMap(): Map<Class<out ViewModel>, Provider<out ViewModel>> = mutableMapOf()

    @Singleton
    @Provides
    fun provideTimeProvider() = TimeProvider()

    @Singleton
    @Provides
    fun provideResourceProvider(context: Context): ResourceProvider = ResourceProviderImpl(context)

    @Singleton
    @Provides
    fun provideSemanticTimeProvider(
        timeProvider: TimeProvider,
        resourceProvider: ResourceProvider
    ) = SemanticTimeProvider(
        timeProvider = timeProvider,
        resourceProvider = resourceProvider
    )

    @Singleton
    @Provides
    fun provideLoggerFactory(): LoggerFactory = LoggerFactoryImpl()

    @Singleton
    @Provides
    fun provideUrlValidator() = UrlValidator()

    @Singleton
    @Provides
    fun providePicassoWrap(
        appSettings: AppSettings,
        meteredConnectionChecker: MeteredConnectionChecker
    ): PicassoWrap = PicassoWrapImpl(
        appSettings = appSettings,
        meteredConnectionChecker = meteredConnectionChecker
    )

    @Singleton
    @Provides
    open fun provideMeteredConnectionChecker(context: Context): MeteredConnectionChecker = MeteredConnectionCheckerImpl(context)

    @Singleton
    @Provides
    fun provideFaviconBitmapProvider(
        loggerFactory: LoggerFactory
    ) = FaviconBitmapProvider(
        loggerFactory = loggerFactory
    )

    @Singleton
    @Provides
    fun provideActionTokenProvider() = ActionTokenProvider()

}