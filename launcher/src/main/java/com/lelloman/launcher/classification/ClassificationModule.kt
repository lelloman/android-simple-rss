package com.lelloman.launcher.classification

import com.lelloman.launcher.packages.PackageLaunchDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ClassificationModule {

    @Provides
    @Singleton
    fun providePackageLaunchClassifier(
        packageLaunchDao: PackageLaunchDao
    ): PackageClassifier = PackageClassifierImpl(
        packageLaunchDao = packageLaunchDao
    )
}