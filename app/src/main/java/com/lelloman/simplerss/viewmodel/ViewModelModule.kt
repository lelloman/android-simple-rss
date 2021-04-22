package com.lelloman.simplerss.viewmodel

import android.content.Context
import com.lelloman.simplerss.ui_base.IoScheduler
import com.lelloman.simplerss.ui_base.ResourcesProvider
import com.lelloman.simplerss.ui_base.UiScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    fun provideResourcesProvider(@ApplicationContext context: Context): ResourcesProvider {
        return ContextResourcesProvider(context)
    }

    @Provides
    @IoScheduler
    fun provideIoScheduler(): Scheduler = Schedulers.io()

    @Provides
    @UiScheduler
    fun provideUiScheduler(): Scheduler = AndroidSchedulers.mainThread()
}