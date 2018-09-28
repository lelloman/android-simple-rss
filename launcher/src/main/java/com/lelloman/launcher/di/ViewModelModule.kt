package com.lelloman.launcher.di

import android.arch.lifecycle.ViewModel
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.launcher.MainViewModel
import com.lelloman.launcher.MainViewModelImpl
import dagger.Module
import dagger.Provides
import javax.inject.Provider
import javax.inject.Singleton

@Module
open class ViewModelModule {

    @Singleton
    @Provides
    fun provideMap(): Map<Class<out ViewModel>, Provider<out ViewModel>> = mutableMapOf()

    @Provides
    open fun provideMainViewModel(
        dependencies: BaseViewModel.Dependencies
    ): MainViewModel = MainViewModelImpl(
        dependencies = dependencies
    )
}