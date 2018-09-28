package com.lelloman.launcher.di

import com.lelloman.common.di.BaseApplicationModule
import com.lelloman.common.settings.BaseSettingsModule
import com.lelloman.launcher.LauncherApplication
import com.lelloman.launcher.packages.PackagesModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidContributes::class,
    AndroidInjectionModule::class,
    BaseApplicationModule::class,
    BaseSettingsModule::class,
    PackagesModule::class,
    ViewModelFactoryModule::class,
    ViewModelModule::class
])
interface AppComponent {

    fun inject(app: LauncherApplication)
}