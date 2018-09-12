package com.lelloman.read.core.navigation

import android.content.Context

interface DeepLinkStartable {
    fun start(context: Context, deepLink: DeepLink)
}