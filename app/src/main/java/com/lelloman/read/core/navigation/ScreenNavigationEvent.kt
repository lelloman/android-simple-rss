package com.lelloman.read.core.navigation

class ScreenNavigationEvent(
    val targetClass: NavigationScreen,
    vararg val args: Any = emptyArray()
) : NavigationEvent