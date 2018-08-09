package com.lelloman.read.core.navigation

open class ScreenNavigationEvent(
    val targetClass: NavigationScreen,
    val args: Array<Any> = emptyArray()
) : NavigationEvent