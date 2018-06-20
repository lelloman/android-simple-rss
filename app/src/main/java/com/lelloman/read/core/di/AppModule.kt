package com.lelloman.read.core.di

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.content.Context
import com.lelloman.read.core.ResourceProvider
import com.lelloman.read.core.ResourceProviderImpl
import com.lelloman.read.core.TimeDiffCalculator
import com.lelloman.read.core.TimeProvider
import com.lelloman.read.core.di.qualifiers.IoScheduler
import com.lelloman.read.core.di.qualifiers.NewThreadScheduler
import com.lelloman.read.core.di.qualifiers.UiScheduler
import com.lelloman.read.core.logger.LoggerFactory
import com.lelloman.read.core.logger.LoggerFactoryImpl
import com.lelloman.read.core.navigation.NavigationRouter
import com.lelloman.read.utils.UrlValidator
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Provider
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

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
    fun provideTimeDiffCalculator(
        timeProvider: TimeProvider,
        resourceProvider: ResourceProvider
    ) = TimeDiffCalculator(
        timeProvider = timeProvider,
        resourceProvider = resourceProvider
    )

    @Singleton
    @Provides
    fun provideLoggerFactory(): LoggerFactory = LoggerFactoryImpl()

    @Singleton
    @Provides
    fun provideUrlValidator() = UrlValidator()
}