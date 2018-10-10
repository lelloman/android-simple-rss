package com.lelloman.launcher.classification

import com.lelloman.common.di.qualifiers.IoScheduler
import com.lelloman.common.utils.TimeProvider
import com.lelloman.launcher.logger.LauncherLoggerFactory
import com.lelloman.launcher.packages.PackagesManager
import com.lelloman.launcher.persistence.ClassifiedIdentifierDao
import com.lelloman.launcher.persistence.PackageLaunchDao
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import javax.inject.Singleton

@Module
class ClassificationModule {

    @Provides
    fun provideTimeEncoder() = TimeEncoder()

    @Provides
    fun providePackageLaunchEncoderProvider(): IdentifierEncoderProvider = object : IdentifierEncoderProvider {
        override fun provideEncoder(identifiers: List<String>) = IdentifierEncoder(identifiers)
    }

    @Provides
    @Singleton
    fun providePackageLaunchClassifier(
        packageLaunchDao: PackageLaunchDao,
        loggerFactory: LauncherLoggerFactory,
        timeProvider: TimeProvider,
        timeEncoder: TimeEncoder,
        packagesManager: PackagesManager,
        classifiedIdentifierDao: ClassifiedIdentifierDao,
        @IoScheduler ioScheduler: Scheduler,
        launhesEncoderProvider: IdentifierEncoderProvider
    ): PackageClassifier = PackageClassifier(
        packageLaunchDao = packageLaunchDao,
        launchesEncoderProvider = launhesEncoderProvider,
        loggerFactory = loggerFactory,
        timeEncoder = timeEncoder,
        timeProvider = timeProvider,
        packagesManager = packagesManager,
        ioScheduler = ioScheduler,
        classifiedIdentifierDao = classifiedIdentifierDao
    )
}