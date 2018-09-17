package com.lelloman.deviceinfo.di

import android.arch.lifecycle.ViewModel
import com.lelloman.common.utils.ActionTokenProvider
import com.lelloman.common.view.ResourceProvider
import com.lelloman.deviceinfo.InfoListViewModel
import com.lelloman.deviceinfo.InfoListViewModelImpl
import dagger.Module
import dagger.Provides
import javax.inject.Provider
import javax.inject.Singleton

@Module
open class ViewModelModule {

    @Singleton
    @Provides
    fun provideMap(): Map<Class<out ViewModel>, Provider<out ViewModel>> = mutableMapOf()

    @Provides
    open fun provideInfoListViewModel(
        resourceProvider: ResourceProvider,
        actionTokenProvider: ActionTokenProvider
    ): InfoListViewModel = InfoListViewModelImpl(
        resourceProvider = resourceProvider,
        actionTokenProvider = actionTokenProvider
    )
}