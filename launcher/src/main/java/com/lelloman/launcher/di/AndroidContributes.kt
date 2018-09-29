package com.lelloman.launcher.di

import com.lelloman.launcher.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface AndroidContributes {

    @ContributesAndroidInjector
    fun contributesMainActivity(): MainActivity
}