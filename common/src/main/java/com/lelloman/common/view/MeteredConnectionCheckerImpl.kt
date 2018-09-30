package com.lelloman.common.view

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.support.annotation.RequiresPermission

class MeteredConnectionCheckerImpl(context: Context) : MeteredConnectionChecker {

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override fun isNetworkMetered() = connectivityManager.isActiveNetworkMetered
}