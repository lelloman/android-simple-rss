package com.lelloman.deviceinfo.device

import com.lelloman.common.utils.model.Resolution
import io.reactivex.Observable

interface Device {

    val screenResolutionPx: Observable<Resolution>
    val screenResolutionDp: Observable<Resolution>
    val screenDensityDpi: Observable<Int>

//    val networkInterfaces: Observable<List<NetworkInterface>>
}

