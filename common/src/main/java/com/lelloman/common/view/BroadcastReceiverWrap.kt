package com.lelloman.common.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class BroadcastReceiverWrap(private val context: Context) {

    private val broadcastsSubject = PublishSubject.create<Intent>()
    val broadcasts: Observable<Intent> = broadcastsSubject.hide()

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            broadcastsSubject.onNext(intent)
        }
    }

    fun register(
        dataScheme: String?,
        actions: Array<String>
    ) {
        context.registerReceiver(broadcastReceiver, IntentFilter().apply {
            actions.forEach(::addAction)
            dataScheme?.let { addDataScheme(it) }
        })
    }

    @Suppress("unused")
    fun register(actions: Array<String>) = register(
        dataScheme = null,
        actions = actions
    )

    @Suppress("unused")
    fun unregister() = context.unregisterReceiver(broadcastReceiver)
}