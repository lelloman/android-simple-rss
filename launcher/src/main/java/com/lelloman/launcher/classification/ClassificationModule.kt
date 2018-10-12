package com.lelloman.launcher.classification

import com.lelloman.common.LLContext
import com.lelloman.launcher.logger.LauncherLoggerFactory
import com.lelloman.launcher.packages.PackagesManager
import com.lelloman.launcher.persistence.PersistentClassificationInfo
import com.lelloman.launcher.persistence.db.ClassifiedIdentifierDao
import com.lelloman.launcher.persistence.db.PackageLaunchDao
import com.lelloman.nn.Network
import dagger.Module
import dagger.Provides
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
    fun provideClassifierPersistence(): ClassifierPersistence = object : ClassifierPersistence {
        override var lastClassificationTimeMs: Long
            get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
            set(value) {}

        override fun loadClassifier(): Network {
            TODO("implement me")
        }

        override fun saveClassifier(network: Network) {
            TODO("implement me")
        }
    }

    @Provides
    fun provideNnFactory(llContext: LLContext) = NnFactory(llContext)

    @Provides
    fun provideClassificationTrigger(
        llContext: LLContext,
        packageClassifier: PackageClassifier,
        classifierPersistence: ClassifierPersistence
    ) = ClassificationTrigger(
        packageClassifier = packageClassifier,
        llContext = llContext,
        classifierPersistence = classifierPersistence
    )

    @Provides
    @Singleton
    fun providePackageLaunchClassifier(
        packageLaunchDao: PackageLaunchDao,
        loggerFactory: LauncherLoggerFactory,
        llContext: LLContext,
        timeEncoder: TimeEncoder,
        packagesManager: PackagesManager,
        classifiedIdentifierDao: ClassifiedIdentifierDao,
        identifierEncoderProvider: IdentifierEncoderProvider,
        persistentClassificationInfo: PersistentClassificationInfo,
        classifierPersistence: ClassifierPersistence,
        nnFactory: NnFactory
    ): PackageClassifier = PackageClassifier(
        llContext = llContext,
        nnFactory = nnFactory,
        packageLaunchDao = packageLaunchDao,
        persistentClassificationInfo = persistentClassificationInfo,
        identifierEncoderProvider = identifierEncoderProvider,
        loggerFactory = loggerFactory,
        timeEncoder = timeEncoder,
        packagesManager = packagesManager,
        classifiedIdentifierDao = classifiedIdentifierDao,
        classifierPersistence = classifierPersistence
    )
}