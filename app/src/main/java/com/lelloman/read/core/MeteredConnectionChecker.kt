package com.lelloman.read.core

import android.content.Context
import android.net.ConnectivityManager

open class MeteredConnectionChecker(context: Context) {

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    open fun isNetworkMetered() = connectivityManager.isActiveNetworkMetered
}