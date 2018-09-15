package com.lelloman.common.di

import com.lelloman.common.BaseApplication
import com.lelloman.common.settings.BaseSettingsModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    BaseApplicationModule::class,
    BaseSettingsModule::class
])
interface BaseApplicationComponent {

    fun inject(app: BaseApplication)
}