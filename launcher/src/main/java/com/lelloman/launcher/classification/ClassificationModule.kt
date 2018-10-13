package com.lelloman.launcher.classification

import android.content.Context
import com.lelloman.common.LLContext
import com.lelloman.launcher.logger.LauncherLoggerFactory
import com.lelloman.launcher.packages.PackagesManager
import com.lelloman.launcher.persistence.ClassifierPersistence
import com.lelloman.launcher.persistence.ClassifierPersistenceImpl
import com.lelloman.launcher.persistence.PersistentClassificationInfo
import com.lelloman.launcher.persistence.db.ClassifiedIdentifierDao
import com.lelloman.launcher.persistence.db.PackageLaunchDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ClassificationModule {

    @Provides
    fun provideTimeEncoder() = TimeEncoder()

    @Provides
    @Singleton
    fun provideClassifierPersistence(context: Context): ClassifierPersistence = ClassifierPersistenceImpl(context = context)

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
        persistentClassificationInfo: PersistentClassificationInfo,
        classifierPersistence: ClassifierPersistence,
        nnFactory: NnFactory
    ): PackageClassifier = PackageClassifier(
        llContext = llContext,
        nnFactory = nnFactory,
        packageLaunchDao = packageLaunchDao,
        persistentClassificationInfo = persistentClassificationInfo,
        loggerFactory = loggerFactory,
        timeEncoder = timeEncoder,
        packagesManager = packagesManager,
        classifiedIdentifierDao = classifiedIdentifierDao,
        classifierPersistence = classifierPersistence
    )
}