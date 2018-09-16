package com.lelloman.deviceinfo.device

import com.lelloman.common.utils.model.Resolution
import io.reactivex.Observable

interface Device {

    val screenResolution: Observable<Resolution>
    val screenSizeDp: Observable<Resolution>
    val screenDensityDpi: Observable<Int>

//    val networkInterfaces: Observable<List<NetworkInterface>>
}

