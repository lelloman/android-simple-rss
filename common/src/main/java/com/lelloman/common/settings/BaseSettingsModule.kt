package com.lelloman.common.settings

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
open class BaseSettingsModule {

    @Provides
    open fun provideBaseApplicationSettings(context: Context)
        : BaseApplicationSettings = BaseApplicationSettingsImpl(context)
}