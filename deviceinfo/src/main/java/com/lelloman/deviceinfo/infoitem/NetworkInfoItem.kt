package com.lelloman.deviceinfo.infoitem

import com.lelloman.deviceinfo.device.NetworkInterface

class NetworkInfoItem(
    override val id: Long,
    val networkInterfaces: List<NetworkInterface>
) : InfoItem