package com.lelloman.deviceinfo.device

import com.lelloman.common.utils.model.ModelWithId

data class NetworkInterface(
    override val id: Long,
    val name: String,
    val hwAddress: String,
    val netAddresses: List<String>
) : ModelWithId