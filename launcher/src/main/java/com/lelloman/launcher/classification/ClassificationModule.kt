package com.lelloman.launcher.classification

import com.lelloman.common.logger.LoggerFactory
import com.lelloman.launcher.persistence.PackageLaunchDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ClassificationModule {

    @Provides
    @Singleton
    fun provideNeuralNetFactory() = NeuralNetFactory()

    @Provides
    @Singleton
    fun providePackageLaunchClassifier(
        packageLaunchDao: PackageLaunchDao,
        loggerFactory: LoggerFactory,
        neuralNetFactory: NeuralNetFactory
    ): PackageClassifier = PackageClassifierImpl(
        packageLaunchDao = packageLaunchDao,
        loggerFactory = loggerFactory,
        neuralNetFactory = neuralNetFactory
    )
}