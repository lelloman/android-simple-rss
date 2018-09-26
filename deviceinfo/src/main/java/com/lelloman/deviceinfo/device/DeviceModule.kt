package com.lelloman.deviceinfo.device

import android.content.Context
import dagger.Module
import dagger.Provides
import io.reactivex.Observable

@Module
class DeviceModule {

    @Provides
    fun provideDevice(
        context: Context,
        configurationChanges: Observable<Any>
    ): Device = DeviceImpl(
        context = context,
        configurationChanges = configurationChanges
    )
}