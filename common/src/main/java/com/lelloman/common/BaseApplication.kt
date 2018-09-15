package com.lelloman.common

import android.app.Application
import com.lelloman.common.di.BaseApplicationModule
import com.lelloman.common.di.DaggerBaseApplicationComponent
import com.lelloman.common.settings.BaseSettingsModule

abstract class BaseApplication : Application() {

    protected val baseSettingsModule = BaseSettingsModule()

    override fun onCreate() {
        super.onCreate()
        instance = this
        inject()
    }

    protected open fun inject() {
        DaggerBaseApplicationComponent.builder()
            .baseApplicationModule(BaseApplicationModule(this))
            .baseSettingsModule(baseSettingsModule)
            .build()
            .inject(this)
    }

    companion object {
        private lateinit var instance: BaseApplication

    }
}