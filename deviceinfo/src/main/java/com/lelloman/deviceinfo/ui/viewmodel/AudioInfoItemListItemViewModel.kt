package com.lelloman.deviceinfo.ui.viewmodel

import com.lelloman.common.view.ResourceProvider
import com.lelloman.deviceinfo.R
import com.lelloman.deviceinfo.infoitem.AudioInfoItem

class AudioInfoItemListItemViewModel(
    private val resourceProvider: ResourceProvider
) : InfoItemListItemViewModel<AudioInfoItem> {

    var mode = ""
        private set

    override fun bind(item: AudioInfoItem) {
        mode = resourceProvider.getString(R.string.audio_mode, item.audioMode.name)
    }
}