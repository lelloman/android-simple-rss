package com.lelloman.read.core.navigation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import kotlin.reflect.KFunction
import kotlin.reflect.KType
import kotlin.reflect.KVisibility
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.createType
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.jvm.jvmErasure

class NavigationRouter {

    fun onNavigationEvent(activity: Activity, navigationEvent: NavigationEvent) = when (navigationEvent) {
        is ScreenNavigationEvent -> navigateToActivity(activity, navigationEvent)
        is CloseScreenNavigationEvent -> activity.finish()
        is ViewIntentNavigationEvent -> {
            val intent = Intent(Intent.ACTION_VIEW)
                .setData(Uri.parse(navigationEvent.url))
            activity.startActivity(intent)
        }
        else -> {
        }
    }

    private fun navigateToActivity(activity: Activity, event: ScreenNavigationEvent) {

        val starterMethod = findStarterMethod(event.screen, event.args)
            ?: throw IllegalArgumentException("Could not find starter method for screen ${event.screen} with args ${event.args}")

        starterMethod.call(event.screen.clazz.companionObject!!.objectInstance, activity, *event.args)

        if (event is ScreenAndCloseNavigationEvent) {
            activity.finish()
        }
    }

    internal fun findStarterMethod(navigationScreen: NavigationScreen, args: Array<Any>): KFunction<*>? = navigationScreen
        .clazz
        .companionObject
        ?.declaredFunctions
        ?.filter {
            when {
                !it.name.startsWith("start") -> false
                it.visibility != KVisibility.PUBLIC -> false
                it.parameters.size != args.size + 2 -> false
                it.parameters[1].type.jvmErasure.java != Activity::class.java -> false
                else -> true
            }
        }
        ?.firstOrNull findFirst@{
            for (i in 2 until it.parameters.size) {
                if (it.parameters[i].type.jvmErasure.java != args[i - 2]::class.java) {
                    return@findFirst false
                }
            }
            true
        }
}