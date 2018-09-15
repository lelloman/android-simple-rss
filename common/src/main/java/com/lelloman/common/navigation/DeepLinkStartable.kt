package com.lelloman.common.navigation

import android.content.Context

interface DeepLinkStartable {
    fun start(context: Context, deepLink: DeepLink)
}