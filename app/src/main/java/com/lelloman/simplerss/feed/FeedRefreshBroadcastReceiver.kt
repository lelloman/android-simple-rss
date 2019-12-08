package com.lelloman.simplerss.feed

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * ./adb shell am broadcast -a com.lelloman.simplerss.feed.ACTION_TRIGGER_REFRESH -n com.lelloman.simplerss/.feed.Fee_TRIGGER_REFRESH -n com.lelloman.simplerss/.feed.FeedRefreshBroadcastReceiver
 */
class FeedRefreshBroadcastReceiver : BroadcastReceiver(), KoinComponent {

    private val feedRefresher by inject<FeedRefresher>()

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == ACTION_TRIGGER_REFRESH) {
            feedRefresher.refresh()
        }
    }

    private companion object {
        const val ACTION_TRIGGER_REFRESH = "ACTION_TRIGGER_REFRESH"
    }
}