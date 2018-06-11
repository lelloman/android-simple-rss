package com.lelloman.read.core.navigation

import android.app.Activity

class NavigationRouter{

    fun onNavigationEvent(activity: Activity, navigationEvent: NavigationEvent) = when(navigationEvent){
        is ScreenNavigationEvent -> navigateToActivity(activity, navigationEvent)
        is BackNavigationEvent -> activity.onBackPressed()
        else -> {}
    }

    private fun navigateToActivity(activity: Activity, event: ScreenNavigationEvent){
        event.targetClass.starters.forEach {
            it.call(activity, *event.args)
        }
    }
}