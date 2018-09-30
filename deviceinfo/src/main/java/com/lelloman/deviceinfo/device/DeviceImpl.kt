package com.lelloman.deviceinfo.device

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Point
import android.media.AudioManager
import android.net.ConnectivityManager
import android.view.WindowManager
import com.lelloman.common.utils.model.Resolution
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject

class DeviceImpl(
    private val context: Context,
    private val configurationChanges: Observable<Any>
) : Device {
    private val audioManager by lazy { context.getSystemService(Context.AUDIO_SERVICE) as AudioManager }
    private val windowManager by lazy { context.getSystemService(Context.WINDOW_SERVICE) as WindowManager }
    private val displayMetrics get() = context.resources.displayMetrics

    private val screenResolutionSubject = BehaviorSubject.create<Resolution>()
    private val screenDensityDpiSubject = BehaviorSubject.create<Int>()
    private val screenSizeDpSubject = BehaviorSubject.create<Resolution>()

    private val networkInterfacesSubject = BehaviorSubject.create<List<NetworkInterface>>()

    private val audioModeSubject = BehaviorSubject.create<AudioMode>()

    override val screenResolutionPx: Observable<Resolution> = screenResolutionSubject.hide()
    override val screenDensityDpi: Observable<Int> = screenDensityDpiSubject.hide()
    override val screenResolutionDp: Observable<Resolution> = screenSizeDpSubject.hide()

    override val networkInterfaces: Observable<List<NetworkInterface>> = networkInterfacesSubject.hide()

    override val audioMode: Observable<AudioMode> = audioModeSubject.hide()

    private val connectivityActionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                readNetworkInterfaces()
            }
        }
    }

    private val subscriptions = CompositeDisposable()

    init {
        context.registerReceiver(connectivityActionReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        readScreen()

        readNetworkInterfaces()

        audioModeSubject.onNext(AudioMode.fromAudioManagerMode(audioManager.mode))

        subscription {
            configurationChanges
                .subscribe {
                    readScreen()
                }
        }
    }

    override fun dispose() {
        subscriptions.clear()
    }

    private fun subscription(block: () -> Disposable) {
        subscriptions.add(block.invoke())
    }

    private fun readScreen() {
        readScreenResolution()
        readScreenDensity()
        readScreenSizeDp()
    }

    private fun readScreenResolution() = Point()
        .apply { windowManager.defaultDisplay.getRealSize(this) }
        .let { Resolution(width = it.x, height = it.y) }
        .apply(screenResolutionSubject::onNext)

    private fun readScreenDensity() {
        screenDensityDpiSubject.onNext(displayMetrics.densityDpi)
    }

    private fun readScreenSizeDp() = screenResolutionPx
        .blockingFirst()
        .let {
            val density = displayMetrics.density
            Resolution(
                width = Math.round(it.width / density),
                height = Math.round(it.height / density)
            )
        }
        .apply(screenSizeDpSubject::onNext)

    private fun readNetworkInterfaces() = java.net.NetworkInterface
        .getNetworkInterfaces()
        .toList()
        .filter { it.inetAddresses.toList().isNotEmpty() || it.hardwareAddress != null }
        .mapIndexed { index, it ->
            NetworkInterface(
                id = index.toLong(),
                name = it.displayName,
                netAddresses = it.inetAddresses.toList().map { it.toString() },
                hwAddress = it.hardwareAddress?.joinToString { java.lang.Integer.toHexString(it.toInt()) }
                    ?: "-"
            )
        }
        .also { networkInterfacesSubject.onNext(it) }
}