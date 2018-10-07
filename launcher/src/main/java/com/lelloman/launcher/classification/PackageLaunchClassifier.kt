package com.lelloman.launcher.classification

import com.lelloman.launcher.classification.model.PackageLaunchesDataset
import com.lelloman.launcher.packages.Package
import com.lelloman.launcher.persistence.model.PackageLaunch


class PackageLaunchClassifier(
    private val launches: List<PackageLaunch>,
    private val launchesEncoder: PackageLaunchEncoder
) {
    fun classify(packageLaunch: Package): Double = 0.0
    fun train(dataset: PackageLaunchesDataset) = Unit
}
