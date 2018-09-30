package com.lelloman.deviceinfo.di

import com.lelloman.deviceinfo.DeviceInfoApplication
import dagger.Module
import dagger.Provides
import io.reactivex.Observable

@Module
class AppModule(private val app: DeviceInfoApplication) {

    @Provides
    fun provideConfigurationChanges(): Observable<Any> = app.configurationChanges
}