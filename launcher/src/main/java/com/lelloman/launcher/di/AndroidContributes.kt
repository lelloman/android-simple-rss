package com.lelloman.launcher.di

import com.lelloman.launcher.ui.launches.view.LaunchesActivity
import com.lelloman.launcher.ui.main.view.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
interface AndroidContributes {

    @ContributesAndroidInjector
    fun contributesMainActivity(): MainActivity

    @ContributesAndroidInjector
    fun contributesLaunchesActivity(): LaunchesActivity
}