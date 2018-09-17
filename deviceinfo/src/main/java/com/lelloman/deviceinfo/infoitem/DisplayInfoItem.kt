package com.lelloman.deviceinfo.infoitem

import com.lelloman.common.utils.model.Resolution

data class DisplayInfoItem(
    override val id: Long,
    val screenDensityDpi: Int,
    val screenResolutionPx: Resolution,
    val screenResolutionDp: Resolution
) : InfoItem