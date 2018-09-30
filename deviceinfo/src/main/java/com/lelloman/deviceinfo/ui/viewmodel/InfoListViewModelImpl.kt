package com.lelloman.deviceinfo.ui.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.lelloman.common.di.qualifiers.IoScheduler
import com.lelloman.common.utils.LazyLiveData
import com.lelloman.common.utils.model.Resolution
import com.lelloman.deviceinfo.device.Device
import com.lelloman.deviceinfo.infoitem.AudioInfoItem
import com.lelloman.deviceinfo.infoitem.DisplayInfoItem
import com.lelloman.deviceinfo.infoitem.InfoItem
import com.lelloman.deviceinfo.infoitem.NetworkInfoItem
import io.reactivex.Observable
import io.reactivex.Scheduler

class InfoListViewModelImpl(
    @IoScheduler private val ioScheduler: Scheduler,
    private val device: Device,
    dependencies: Dependencies
) : InfoListViewModel(dependencies) {

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

    private val networkInfoItem by lazy {
        device
            .networkInterfaces
            .map {
                NetworkInfoItem(
                    id = 2L,
                    networkInterfaces = it
                )
            }
    }

    private val audioInfoItem by lazy {
        device
            .audioMode
            .map {
                AudioInfoItem(
                    id = 3,
                    audioMode = it
                )
            }
    }

    @Suppress("UNCHECKED_CAST")
    override val deviceInfos: MutableLiveData<List<InfoItem>> by LazyLiveData {
        subscription {
            Observable
                .combineLatest(
                    arrayOf(
                        displayInfoItem,
                        networkInfoItem,
                        audioInfoItem
                    )
                ) { it.toList() as List<InfoItem> }
                .subscribeOn(ioScheduler)
                .observeOn(ioScheduler)
                .subscribe {
                    deviceInfos.postValue(it)
                }
        }
    }

    override fun onInfoItemClicked(infoItem: InfoItem) = when (infoItem) {
        is DisplayInfoItem -> {
            shortToast("open display screen")
        }
        else -> Unit
    }
}