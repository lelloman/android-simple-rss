package com.lelloman.read.core

import android.net.Uri
import android.support.annotation.DrawableRes
import android.widget.ImageView
import com.lelloman.read.feed.MeteredConnectionChecker
import com.lelloman.read.persistence.settings.AppSettings
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import javax.inject.Inject

class PicassoWrap @Inject constructor(
    private val appSettings: AppSettings,
    private val meteredConnectionChecker: MeteredConnectionChecker
) {

    fun loadUrlIntoImageView(
        url: String,
        view: ImageView,
        @DrawableRes placeHolderId: Int? = null
    ) {
        val canUseNetwork = appSettings.useMeteredNetwork.blockingFirst() || !meteredConnectionChecker.isNetworkMetered()

        var requestCreator = Picasso.get()
            .load(Uri.parse(url))

        placeHolderId?.let {
            requestCreator.placeholder(it)
        }

        if (!canUseNetwork) {
            requestCreator = requestCreator.networkPolicy(NetworkPolicy.OFFLINE)
        }

        requestCreator.into(view)
    }
}