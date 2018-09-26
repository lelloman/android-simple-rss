package com.lelloman.deviceinfo.testutils

import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.deviceinfo.device.Device
import com.lelloman.deviceinfo.di.ViewModelModule
import com.lelloman.deviceinfo.ui.InfoListViewModel
import io.reactivex.Scheduler
import org.mockito.Mockito.mock

class MockViewModelModule : ViewModelModule() {

    var infoListViewModel = mock(InfoListViewModel::class.java)

    override fun provideInfoListViewModel(ioScheduler: Scheduler, device: Device, dependencies: BaseViewModel.Dependencies): InfoListViewModel {
        return infoListViewModel
    }
}