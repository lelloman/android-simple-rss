package com.lelloman.common.navigation

class PackageIntentNavigationEvent(
    val packageName: String,
    val activityName: String?
) : NavigationEvent