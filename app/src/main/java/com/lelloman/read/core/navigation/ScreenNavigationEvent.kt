package com.lelloman.read.core.navigation

class ScreenNavigationEvent(
    val targetClass: NavigationScreen,
    val args: Array<Any> = emptyArray()
) : NavigationEvent