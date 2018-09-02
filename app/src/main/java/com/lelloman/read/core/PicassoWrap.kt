package com.lelloman.read.core

import android.net.Uri
import android.support.annotation.DrawableRes
import android.widget.ImageView

interface PicassoWrap {

    fun enableImageSourceIndicator()

    fun loadUrlIntoImageView(
        uri: Uri,
        view: ImageView,
        @DrawableRes placeHolderId: Int? = null
    )
}