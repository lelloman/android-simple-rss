package com.lelloman.deviceinfo.di

import com.lelloman.deviceinfo.ui.InfoListActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface AndroidContributes {

    @ContributesAndroidInjector
    fun contributeInfoListActivity(): InfoListActivity
}