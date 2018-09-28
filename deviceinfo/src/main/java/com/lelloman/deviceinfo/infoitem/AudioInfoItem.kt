package com.lelloman.deviceinfo.infoitem

import com.lelloman.deviceinfo.device.AudioMode

data class AudioInfoItem(
    override val id: Long,
    val audioMode: AudioMode
) : InfoItem