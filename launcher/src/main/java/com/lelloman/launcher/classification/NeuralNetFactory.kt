package com.lelloman.launcher.classification

import com.lelloman.launcher.persistence.model.PackageLaunch

class NeuralNetFactory {

    fun createLaunchesEncoder(launches: List<PackageLaunch>) = PackageLaunchEncoder(launches)

    fun createPackageLaunchClassifier(launches: List<PackageLaunch>, launchesEncoder: PackageLaunchEncoder) =
        PackageLaunchClassifier(launches, launchesEncoder)
}