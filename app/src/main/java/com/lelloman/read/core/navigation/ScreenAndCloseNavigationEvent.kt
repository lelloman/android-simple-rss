package com.lelloman.read.core.navigation

class ScreenAndCloseNavigationEvent(
    targetClass: NavigationScreen,
    args: Array<Any> = emptyArray()
) : ScreenNavigationEvent(
    targetClass = targetClass,
    args = args
)