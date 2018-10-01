package com.lelloman.launcher.di

import android.arch.lifecycle.ViewModel
import com.lelloman.common.di.qualifiers.IoScheduler
import com.lelloman.common.utils.TimeProvider
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.launcher.packages.PackageLaunchDao
import com.lelloman.launcher.packages.PackagesManager
import com.lelloman.launcher.ui.viewmodel.MainViewModel
import com.lelloman.launcher.ui.viewmodel.MainViewModelImpl
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import javax.inject.Provider
import javax.inject.Singleton

@Module
open class ViewModelModule {

    @Singleton
    @Provides
    fun provideMap(): Map<Class<out ViewModel>, Provider<out ViewModel>> = mutableMapOf()

    @Provides
    open fun provideMainViewModel(
        @IoScheduler ioScheduler: Scheduler,
        packagesManager: PackagesManager,
        dependencies: BaseViewModel.Dependencies,
        packageLaunchDao: PackageLaunchDao,
        timeProvider: TimeProvider
    ): MainViewModel = MainViewModelImpl(
        ioScheduler = ioScheduler,
        dependencies = dependencies,
        packagesManager = packagesManager,
        packageLaunchDao = packageLaunchDao,
        timeProvider = timeProvider
    )
}