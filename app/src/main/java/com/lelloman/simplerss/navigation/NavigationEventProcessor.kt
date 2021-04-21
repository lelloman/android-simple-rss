package com.lelloman.simplerss.navigation

import androidx.navigation.NavController

interface NavigationEventProcessor {

    operator fun invoke(event: (NavController) -> Unit)
}

class NavigationEventProcessorImpl : NavControllerHolder, NavigationEventProcessor {

    override var navController: NavController? = null

    override fun invoke(event: (NavController) -> Unit) {
        navController?.let(event)
    }
}