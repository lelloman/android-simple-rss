package com.lelloman.deviceinfo.device

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Point
import android.net.ConnectivityManager
import android.view.WindowManager
import com.lelloman.common.utils.model.Resolution
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class DeviceImpl(
    private val context: Context,
    private val configurationChanges: Observable<Any>
) : Device {

    private val connectivityManager by lazy { context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }
    private val windowManager by lazy { context.getSystemService(Context.WINDOW_SERVICE) as WindowManager }
    private val displayMetrics get() = context.resources.displayMetrics

    private val screenResolutionSubject = BehaviorSubject.create<Resolution>()
    private val screenDensityDpiSubject = BehaviorSubject.create<Int>()
    private val screenSizeDpSubject = BehaviorSubject.create<Resolution>()

    private val networkInterfacesSubject = BehaviorSubject.create<List<NetworkInterface>>()

    override val screenResolutionPx: Observable<Resolution> = screenResolutionSubject.hide()
    override val screenDensityDpi: Observable<Int> = screenDensityDpiSubject.hide()
    override val screenResolutionDp: Observable<Resolution> = screenSizeDpSubject.hide()

    override val networkInterfaces: Observable<List<NetworkInterface>> = networkInterfacesSubject.hide()

    private val connectivityActionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                readNetworkInterfaces()
            }
        }
    }

    init {
        context.registerReceiver(connectivityActionReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        readScreen()

        readNetworkInterfaces()

        configurationChanges
            .subscribe {
                readScreen()
            }
    }

    private fun readScreen() {
        readScreenResolution()
        readScreenDensity()
        readScreenSizeDp()
    }

    private fun readScreenResolution() {
        val resolution = Point()
            .apply { windowManager.defaultDisplay.getRealSize(this) }
            .let {
                Resolution(
                    width = it.x,
                    height = it.y
                )
            }
        screenResolutionSubject.onNext(resolution)
    }

    private fun readScreenDensity() {
        screenDensityDpiSubject.onNext(displayMetrics.densityDpi)
    }

    private fun readScreenSizeDp() {
        val screenSizeDp = screenResolutionPx.blockingFirst().let {
            val density = displayMetrics.density
            Resolution(
                width = Math.round(it.width / density),
                height = Math.round(it.height / density)
            )
        }
        screenSizeDpSubject.onNext(screenSizeDp)
    }

    private fun readNetworkInterfaces() = java.net.NetworkInterface
        .getNetworkInterfaces()
        .toList()
        .map {
            NetworkInterface(
                name = it.displayName,
                netAddresses = it.inetAddresses.toList().map { it.toString() },
                hwAddress = it.hardwareAddress?.joinToString { java.lang.Integer.toHexString(it.toInt()) }
                    ?: "-"
            )
        }
        .also { networkInterfacesSubject.onNext(it) }

//    private fun readNetworkInterfaces() = connectivityManager
//        .allNetworks
//        .map(connectivityManager::getNetworkInfo)
//        .map {
//            NetworkInterface(
//                name = it.typeName,
//                hasInternet = it.isConnected
//            )
//        }
//        .also { networkInterfacesSubject.onNext(it) }
}