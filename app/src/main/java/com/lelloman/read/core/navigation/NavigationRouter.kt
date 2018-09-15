package com.lelloman.read.core.navigation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.lelloman.read.core.logger.LoggerFactory

class NavigationRouter(loggerFactory: LoggerFactory) {

    private val logger = loggerFactory.getLogger(NavigationRouter::class.java.simpleName)

    fun onNavigationEvent(activity: Activity, navigationEvent: NavigationEvent) = when (navigationEvent) {
        is DeepLinkNavigationEvent -> handleDeepLink(activity, navigationEvent)
        is CloseScreenNavigationEvent -> activity.finish()
        is ViewIntentNavigationEvent -> {
            val intent = Intent(Intent.ACTION_VIEW)
                .setData(Uri.parse(navigationEvent.url))
            activity.startActivity(intent)
        }
        else -> {
        }
    }

    internal fun handleDeepLink(activity: Activity, event: DeepLinkNavigationEvent) {
        val startable = event.deepLink.screen.deepLinkStartable
        if (startable != null) {
            startable.start(activity, event.deepLink)
            if (event is DeepLinkAndCloseNavigationEvent) {
                activity.finish()
            }
        } else {
            logger.e("Cannot handle deep link screen ${event.deepLink.screen.clazz.qualifiedName} because companion object is not ${DeepLinkStartable::class.java.canonicalName} -> ${event.deepLink}")
        }
    }

}