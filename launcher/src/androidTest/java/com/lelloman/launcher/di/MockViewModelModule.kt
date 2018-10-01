package com.lelloman.launcher.di

import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.launcher.packages.PackagesManager
import com.lelloman.launcher.ui.viewmodel.MainViewModel
import io.reactivex.Scheduler
import org.mockito.Mockito.mock

class MockViewModelModule : ViewModelModule() {

    val mainViewModel: MainViewModel = mock(MainViewModel::class.java)

    override fun provideMainViewModel(
        ioScheduler: Scheduler,
        packagesManager: PackagesManager,
        dependencies: BaseViewModel.Dependencies
    ): MainViewModel = mainViewModel
}