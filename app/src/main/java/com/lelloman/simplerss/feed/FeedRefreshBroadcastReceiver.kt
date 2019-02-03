package com.lelloman.simplerss.feed

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.android.AndroidInjection
import javax.inject.Inject

/**
 * ./adb shell am broadcast -a com.lelloman.simplerss.feed.ACTION_TRIGGER_REFRESH -n com.lelloman.simplerss/.feed.Fee_TRIGGER_REFRESH -n com.lelloman.simplerss/.feed.FeedRefreshBroadcastReceiver
 */
class FeedRefreshBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var feedRefresher: FeedRefresher

    override fun onReceive(context: Context?, intent: Intent?) {
        AndroidInjection.inject(this, context)

        if (intent?.action == ACTION_TRIGGER_REFRESH) {
            feedRefresher.refresh()
        }
    }

    private companion object {
        const val ACTION_TRIGGER_REFRESH = "ACTION_TRIGGER_REFRESH"
    }
}