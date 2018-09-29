package com.lelloman.launcher.di

import android.content.Context
import com.lelloman.common.utils.NavigationBarDetector
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LauncherApplicationModule {

    @Provides
    @Singleton
    fun provideNavigationBarDetector(context: Context) = NavigationBarDetector(context)
}