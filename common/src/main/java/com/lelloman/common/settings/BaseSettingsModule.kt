package com.lelloman.common.settings

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class BaseSettingsModule {

    @Singleton
    @Provides
    open fun provideBaseApplicationSettings(context: Context)
        : BaseApplicationSettings = BaseApplicationSettingsImpl(context)
}