package com.lelloman.deviceinfo.di

import com.lelloman.deviceinfo.ui.view.InfoListActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
interface AndroidContributes {

    @ContributesAndroidInjector
    fun contributeInfoListActivity(): InfoListActivity
}