package com.lelloman.deviceinfo.di

import com.lelloman.common.di.BaseApplicationModule
import com.lelloman.common.settings.BaseSettingsModule
import com.lelloman.deviceinfo.DeviceInfoApplication
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidContributes::class,
    AndroidInjectionModule::class,
    BaseApplicationModule::class,
    BaseSettingsModule::class,
    ViewModelFactoryModule::class,
    ViewModelModule::class
])
interface AppComponent {
    fun inject(app: DeviceInfoApplication)
}