package com.lelloman.read.core.navigation

@Deprecated(message = "use DeepLinkNavigationEvent instead")
open class ScreenNavigationEvent(
    val screen: NavigationScreen,
    val args: Array<out Any> = emptyArray()
) : NavigationEvent