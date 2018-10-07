package com.lelloman.launcher.classification

import com.lelloman.launcher.classification.model.PackageLaunchesDataset
import com.lelloman.launcher.packages.Package
import com.lelloman.launcher.persistence.model.PackageLaunch

interface PackageLaunchEncoder {
}

interface PackageLaunchClassifier {
    fun classify(packageLaunch: Package): Double
    fun train(launches: List<PackageLaunch>, packages: List<Package>, launchesEncoder: PackageLaunchEncoder, dataset: PackageLaunchesDataset)
}

class NeuralNetFactory {

    fun createLaunchesEncoder(launches: List<PackageLaunch>): PackageLaunchEncoder {
        return object : PackageLaunchEncoder {}
    }

    fun createPackageLaunchClassifier(launches: List<PackageLaunch>, launchesEncoder: PackageLaunchEncoder): PackageLaunchClassifier {
        return object : PackageLaunchClassifier {
            override fun classify(packageLaunch: Package) = 0.0
            override fun train(launches: List<PackageLaunch>, packages: List<Package>, launchesEncoder: PackageLaunchEncoder, dataset: PackageLaunchesDataset) {
            }
        }
    }
}