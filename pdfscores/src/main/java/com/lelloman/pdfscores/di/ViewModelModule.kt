package com.lelloman.pdfscores.di

import android.arch.lifecycle.ViewModel
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.pdfscores.recentscores.RecentScoresViewModel
import com.lelloman.pdfscores.recentscores.RecentScoresViewModelImpl
import dagger.Module
import dagger.Provides
import javax.inject.Provider
import javax.inject.Singleton

@Module
class ViewModelModule {

    @Singleton
    @Provides
    fun provideMap(): Map<Class<out ViewModel>, Provider<out ViewModel>> = mutableMapOf()

    @Provides
    open fun provideRecentScoresViewModel(
        dependencies: BaseViewModel.Dependencies
    ): RecentScoresViewModel = RecentScoresViewModelImpl(
        dependencies = dependencies
    )
}