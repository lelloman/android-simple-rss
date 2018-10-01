package com.lelloman.launcher.packages

import android.content.Context
import android.content.pm.PackageManager
import com.lelloman.common.di.qualifiers.IoScheduler
import com.lelloman.common.logger.LoggerFactory
import com.lelloman.launcher.classification.PackageClassifier
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler

@Module
class PackagesModule {

    @Provides
    fun providePackagesManager(
        @IoScheduler ioScheduler: Scheduler,
        context: Context,
        packageManager: PackageManager,
        loggerFactory: LoggerFactory,
        packageClassifier: PackageClassifier
    ) = PackagesManager(
        loggerFactory = loggerFactory,
        context = context,
        ioScheduler = ioScheduler,
        packageManager = packageManager,
        packageClassifier = packageClassifier
    )
}