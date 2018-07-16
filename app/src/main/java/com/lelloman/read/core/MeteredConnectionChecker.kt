package com.lelloman.read.core

import android.content.Context
import android.net.ConnectivityManager
import javax.inject.Inject

class MeteredConnectionChecker @Inject constructor(context: Context) {

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun isNetworkMetered() = connectivityManager.isActiveNetworkMetered
}