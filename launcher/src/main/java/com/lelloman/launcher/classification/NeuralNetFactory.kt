package com.lelloman.launcher.classification

import com.lelloman.common.utils.TimeProvider
import com.lelloman.common.utils.TimeProviderImpl
import com.lelloman.launcher.persistence.model.PackageLaunch
import com.lelloman.nn.dataset.DataSet

class NeuralNetFactory {

    fun createLaunchesEncoder(identifiers: List<String>) = PackageLaunchEncoder(identifiers)

    fun createPackageLaunchClassifier(
        trainingSet: DataSet,
        timeProvider: TimeProvider,
        validationSet: DataSet,
        launchesEncoder: PackageLaunchEncoder
    ) = PackageLaunchClassifier(
        trainingSet = trainingSet,
        timeProvider = timeProvider,
        validationSet = validationSet,
        launchesEncoder = launchesEncoder)

}