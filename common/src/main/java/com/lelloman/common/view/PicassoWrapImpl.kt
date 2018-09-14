package com.lelloman.common.view

import android.support.annotation.DrawableRes
import android.widget.ImageView
import com.lelloman.common.BuildConfig
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import io.reactivex.Observable

class PicassoWrapImpl(
    private val useMeteredNetwork: Observable<Boolean>,
    private val meteredConnectionChecker: MeteredConnectionChecker,
    private val requestCreatorProvider: (uri: String) -> RequestCreator = {
        val picasso = Picasso.get()
        if (BuildConfig.DEBUG) {
            picasso.setIndicatorsEnabled(true)
//            picasso.isLoggingEnabled = true
        }
        picasso.load(it)
    }
) : PicassoWrap {

    override fun enableImageSourceIndicator() {
        Picasso.get().setIndicatorsEnabled(true)
    }

    override fun loadUrlIntoImageView(
        uri: String,
        view: ImageView,
        @DrawableRes placeHolderId: Int?
    ) {
        val canUseNetwork = useMeteredNetwork.blockingFirst() || !meteredConnectionChecker.isNetworkMetered()

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