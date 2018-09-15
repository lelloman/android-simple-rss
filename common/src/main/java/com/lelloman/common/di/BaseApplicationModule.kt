package com.lelloman.common.di

import android.app.Application
import android.content.Context
import com.lelloman.common.di.qualifiers.IoScheduler
import com.lelloman.common.di.qualifiers.NewThreadScheduler
import com.lelloman.common.di.qualifiers.UiScheduler
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

@Module
open class BaseApplicationModule(private val application: Application) {

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

}