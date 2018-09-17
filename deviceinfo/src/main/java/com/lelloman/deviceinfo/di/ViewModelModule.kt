package com.lelloman.deviceinfo.di

import android.arch.lifecycle.ViewModel
import com.lelloman.common.di.qualifiers.IoScheduler
import com.lelloman.common.utils.ActionTokenProvider
import com.lelloman.common.view.ResourceProvider
import com.lelloman.deviceinfo.device.Device
import com.lelloman.deviceinfo.ui.InfoListViewModel
import com.lelloman.deviceinfo.ui.InfoListViewModelImpl
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import javax.inject.Provider
import javax.inject.Singleton

@Module
open class ViewModelModule {

    @Singleton
    @Provides
    fun provideMap(): Map<Class<out ViewModel>, Provider<out ViewModel>> = mutableMapOf()

    @Provides
    open fun provideInfoListViewModel(
        @IoScheduler ioScheduler: Scheduler,
        device: Device,
        resourceProvider: ResourceProvider,
        actionTokenProvider: ActionTokenProvider
    ): InfoListViewModel = InfoListViewModelImpl(
        device = device,
        ioScheduler = ioScheduler,
        resourceProvider = resourceProvider,
        actionTokenProvider = actionTokenProvider
    )
}