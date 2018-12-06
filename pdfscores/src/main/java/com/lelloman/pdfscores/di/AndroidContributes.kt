package com.lelloman.pdfscores.di

import com.lelloman.common.view.InjectableActivity
import com.lelloman.pdfscores.recentscores.view.RecentScoresActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
interface AndroidContributes {

    @ContributesAndroidInjector
    fun contributeBaseActivity(): InjectableActivity

    @ContributesAndroidInjector
    fun contributeRecentScoresActivity(): RecentScoresActivity
}