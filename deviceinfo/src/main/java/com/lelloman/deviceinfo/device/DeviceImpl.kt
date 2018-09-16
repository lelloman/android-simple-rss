package com.lelloman.deviceinfo.device

import android.content.Context
import android.graphics.Point
import android.net.ConnectivityManager
import android.view.WindowManager
import com.lelloman.common.utils.model.Resolution
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class DeviceImpl(private val context: Context) : Device {

    private val connectivityManager by lazy { context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }
    private val windowManager by lazy { context.getSystemService(Context.WINDOW_SERVICE) as WindowManager }
    private val displayMetrics get() = context.resources.displayMetrics

    private val screenResolutionSubject = BehaviorSubject.create<Resolution>()
    private val screenDensityDpiSubject = BehaviorSubject.create<Int>()
    private val screenSizeDpSubject = BehaviorSubject.create<Resolution>()

    override val screenResolution: Observable<Resolution> = screenResolutionSubject.hide()
    override val screenDensityDpi: Observable<Int> = screenDensityDpiSubject.hide()
    override val screenSizeDp: Observable<Resolution> = screenSizeDpSubject.hide()

    init {
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
        val screenSizeDp = screenResolution.blockingFirst().let {
            val density = displayMetrics.density
            Resolution(
                width = Math.round(it.width / density),
                height = Math.round(it.height / density)
            )
        }
        screenSizeDpSubject.onNext(screenSizeDp)
    }

//    override val networkInterfaces: List<NetworkInterface>
//        get() =
//            connectivityManager
//                .allNetworks
//                .map(connectivityManager::getNetworkInfo)
//                .map {
//                    NetworkInterface(
//                        name = it.typeName
//                    )
//                }
}