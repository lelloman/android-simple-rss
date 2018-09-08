package com.lelloman.read.core

import android.support.annotation.DrawableRes
import android.widget.ImageView

interface PicassoWrap {

    fun enableImageSourceIndicator()

    fun loadUrlIntoImageView(
        uri: String,
        view: ImageView,
        @DrawableRes placeHolderId: Int? = null
    )
}