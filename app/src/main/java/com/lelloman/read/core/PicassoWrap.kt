package com.lelloman.read.core

import android.net.Uri
import android.support.annotation.DrawableRes
import android.widget.ImageView
import com.lelloman.read.BuildConfig
import com.lelloman.read.persistence.settings.AppSettings
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator

class PicassoWrap(
    private val appSettings: AppSettings,
    private val meteredConnectionChecker: MeteredConnectionChecker,
    private val requestCreatorProvider: (uri: Uri) -> RequestCreator = {
        val picasso = Picasso.get()
        if (BuildConfig.DEBUG) {
            picasso.setIndicatorsEnabled(true)
            picasso.isLoggingEnabled = true
        }
        picasso.load(it)
    }
) {

    fun enableImageSourceIndicator() {
        Picasso.get().setIndicatorsEnabled(true)
    }

    fun loadUrlIntoImageView(
        uri: Uri,
        view: ImageView,
        @DrawableRes placeHolderId: Int? = null
    ) {
        val canUseNetwork = appSettings.useMeteredNetwork.blockingFirst() || !meteredConnectionChecker.isNetworkMetered()

        var requestCreator = requestCreatorProvider.invoke(uri)

        placeHolderId?.let {
            requestCreator.placeholder(it)
        }

        if (!canUseNetwork) {
            requestCreator = requestCreator.networkPolicy(NetworkPolicy.OFFLINE)
        }

        requestCreator.into(view)
    }
}