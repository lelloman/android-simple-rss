package com.lelloman.simplerss.di

import com.lelloman.common.di.KoinModuleFactory
import com.lelloman.simplerss.persistence.settings.AppSettings
import com.lelloman.simplerss.persistence.settings.AppSettingsImpl
import org.koin.dsl.module

open class SettingsModuleFactory : KoinModuleFactory {

    override fun makeKoinModule(override: Boolean) = module(override=override) {
        single<AppSettings> {
            AppSettingsImpl(
                context = get(),
                baseApplicationSettings = get()
            )
        }
    }
}
