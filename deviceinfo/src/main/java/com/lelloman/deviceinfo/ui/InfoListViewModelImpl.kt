package com.lelloman.deviceinfo.ui

import android.arch.lifecycle.MutableLiveData
import com.lelloman.common.di.qualifiers.IoScheduler
import com.lelloman.common.utils.LazyLiveData
import com.lelloman.common.utils.model.Resolution
import com.lelloman.deviceinfo.device.Device
import com.lelloman.deviceinfo.infoitem.DisplayInfoItem
import com.lelloman.deviceinfo.infoitem.InfoItem
import io.reactivex.Observable
import io.reactivex.Scheduler

class InfoListViewModelImpl(
    @IoScheduler private val ioScheduler: Scheduler,
    private val device: Device,
    dependencies: Dependencies
) : InfoListViewModel(dependencies) {
    override val hello = "asdasdasd"

    private val displayInfoItem by lazy {
        Observable
            .combineLatest(
                arrayOf(
                    device.screenDensityDpi,
                    device.screenResolutionPx,
                    device.screenResolutionDp
                )
            ) { (densityDpi, resolutionPx, resolutionDp) ->
                DisplayInfoItem(
                    id = 1L,
                    screenDensityDpi = densityDpi as Int,
                    screenResolutionPx = resolutionPx as Resolution,
                    screenResolutionDp = resolutionDp as Resolution
                )
            }
    }

    override val deviceInfos: MutableLiveData<List<InfoItem>> by LazyLiveData {
        subscription {
            displayInfoItem
                .map { listOf(it) }
                .subscribeOn(ioScheduler)
                .observeOn(ioScheduler)
                .subscribe {
                    deviceInfos.postValue(it)
                }
        }
    }
}