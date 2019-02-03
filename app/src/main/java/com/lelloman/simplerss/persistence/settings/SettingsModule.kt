package com.lelloman.simplerss.persistence.settings

import android.content.Context
import com.lelloman.common.settings.BaseApplicationSettings
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class SettingsModule {

    @Singleton
    @Provides
    open fun provideAppSettings(
        context: Context,
        baseApplicationSettings: BaseApplicationSettings)
        : AppSettings = AppSettingsImpl(context, baseApplicationSettings)
}
