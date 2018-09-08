package com.lelloman.read.core

import android.content.Context
import android.net.ConnectivityManager

class MeteredConnectionCheckerImpl(context: Context) : MeteredConnectionChecker {

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun isNetworkMetered() = connectivityManager.isActiveNetworkMetered
}