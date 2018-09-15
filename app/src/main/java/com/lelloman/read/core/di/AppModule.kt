package com.lelloman.read.core.di

import android.arch.lifecycle.ViewModel
import android.content.Context
import com.lelloman.common.utils.TimeProvider
import com.lelloman.common.utils.TimeProviderImpl
import com.lelloman.common.utils.UrlValidator
import com.lelloman.common.utils.UrlValidatorImpl
import com.lelloman.common.view.MeteredConnectionChecker
import com.lelloman.common.view.MeteredConnectionCheckerImpl
import com.lelloman.common.view.PicassoWrap
import com.lelloman.common.view.PicassoWrapImpl
import com.lelloman.common.view.ResourceProvider
import com.lelloman.common.view.ResourceProviderImpl
import com.lelloman.read.core.ActionTokenProvider
import com.lelloman.read.core.FaviconBitmapProvider
import com.lelloman.read.core.SemanticTimeProvider
import com.lelloman.read.core.logger.LoggerFactory
import com.lelloman.read.core.logger.LoggerFactoryImpl
import com.lelloman.read.core.navigation.NavigationRouter
import com.lelloman.read.persistence.settings.AppSettings
import dagger.Module
import dagger.Provides
import javax.inject.Provider
import javax.inject.Singleton

@Module
open class AppModule {

    @Provides
    fun provideNavigationRouter(loggerFactory: LoggerFactory) = NavigationRouter(loggerFactory)

    @Singleton
    @Provides
    fun provideMap(): Map<Class<out ViewModel>, Provider<out ViewModel>> = mutableMapOf()

    @Singleton
    @Provides
    fun provideTimeProvider(): TimeProvider = TimeProviderImpl()

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
    fun provideUrlValidator(): UrlValidator = UrlValidatorImpl()

    @Singleton
    @Provides
    fun providePicassoWrap(
        appSettings: AppSettings,
        meteredConnectionChecker: MeteredConnectionChecker
    ): PicassoWrap = PicassoWrapImpl(
        useMeteredNetwork = appSettings.useMeteredNetwork,
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