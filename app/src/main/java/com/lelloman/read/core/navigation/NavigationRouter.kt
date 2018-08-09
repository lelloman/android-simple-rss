package com.lelloman.read.core.navigation

import android.app.Activity
import android.content.Intent
import android.net.Uri

class NavigationRouter{

    fun onNavigationEvent(activity: Activity, navigationEvent: NavigationEvent) = when(navigationEvent){
        is ScreenNavigationEvent -> navigateToActivity(activity, navigationEvent)
        is CloseScreenNavigationEvent -> activity.finish()
        is ViewIntentNavigationEvent -> {
            val intent = Intent(Intent.ACTION_VIEW)
                .setData(Uri.parse(navigationEvent.url))
            activity.startActivity(intent)
        }
        else -> {}
    }

    private fun navigateToActivity(activity: Activity, event: ScreenNavigationEvent){
        event.targetClass.starters.forEach {
            it.call(activity, *event.args)
        }

        if (event is ScreenAndCloseNavigationEvent) {
            activity.finish()
        }
    }
}