package com.lelloman.launcher.classification

import com.lelloman.common.logger.LoggerFactory
import com.lelloman.common.utils.TimeProvider
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
    fun provideTimeEncoder() = TimeEncoder()

    @Provides
    @Singleton
    fun providePackageLaunchClassifier(
        packageLaunchDao: PackageLaunchDao,
        loggerFactory: LoggerFactory,
        neuralNetFactory: NeuralNetFactory,
        timeProvider: TimeProvider,
        timeEncoder: TimeEncoder
    ): PackageClassifier = PackageClassifierImpl(
        packageLaunchDao = packageLaunchDao,
        loggerFactory = loggerFactory,
        timeEncoder = timeEncoder,
        timeProvider = timeProvider,
        neuralNetFactory = neuralNetFactory
    )
}