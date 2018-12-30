package com.lelloman.deviceinfo.ui.viewmodel

import com.lelloman.common.viewmodel.BaseListItemViewModel
import com.lelloman.deviceinfo.device.NetworkInterface

class NetworkInterfaceListItemViewModel : BaseListItemViewModel<Long, NetworkInterface> {

    var name = ""
        private set

    var hwAddress = ""
        private set

    var netAddresses = ""
        private set

    override fun bind(item: NetworkInterface) {
        name = item.name
        hwAddress = item.hwAddress
        netAddresses = item.netAddresses.let { addresses ->
            if (addresses.isNotEmpty()) {
                addresses.joinToString("\n") { it }
            } else {
                "-"
            }
        }
    }
}