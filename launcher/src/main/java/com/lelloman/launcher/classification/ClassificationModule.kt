package com.lelloman.launcher.classification

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ClassificationModule {

    @Provides
    @Singleton
    fun providePackageLaunchClassifier(): PackageClassifier = PackageClassifierImpl()
}