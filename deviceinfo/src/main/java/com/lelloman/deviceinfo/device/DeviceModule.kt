package com.lelloman.deviceinfo.device

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class DeviceModule {

    @Provides
    fun provideDevice(
        context: Context
    ): Device = DeviceImpl(
        context = context
    )
}