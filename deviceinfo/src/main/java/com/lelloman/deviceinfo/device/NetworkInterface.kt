package com.lelloman.deviceinfo.device

data class NetworkInterface(
    val name: String,
    val hwAddress: String,
    val netAddresses: List<String>
)