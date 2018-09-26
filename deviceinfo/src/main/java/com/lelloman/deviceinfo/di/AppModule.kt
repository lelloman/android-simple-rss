package com.lelloman.deviceinfo.di

import com.lelloman.deviceinfo.DeviceInfoApplication
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val app: DeviceInfoApplication) {

    @Provides
    fun provideConfigurationChanges() = app.configurationChanges
}