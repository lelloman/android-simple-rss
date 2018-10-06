package com.lelloman.launcher.packages

import android.content.Context
import android.content.pm.PackageManager
import com.lelloman.common.di.qualifiers.IoScheduler
import com.lelloman.common.logger.LoggerFactory
import com.lelloman.common.view.BroadcastReceiverWrap
import com.lelloman.common.view.ResourceProvider
import com.lelloman.launcher.R
import com.lelloman.launcher.classification.PackageClassifier
import com.lelloman.launcher.di.qualifier.LaunchesActivityPackage
import com.lelloman.launcher.ui.launches.view.LaunchesActivity
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import javax.inject.Singleton

@Module
class PackagesModule {

    @Provides
    @LaunchesActivityPackage
    fun provideLaunchesActivityPackage(
        context: Context,
        resourceProvider: ResourceProvider
    ) = Package(
        id = -1,
        label = "asd asd ",
        packageName = context.packageName,
        activityName = LaunchesActivity::class.java.name,
        drawable = resourceProvider.getDrawable(R.mipmap.ic_launcher)
    )

    @Provides
    fun providePackagesManager(
        @IoScheduler ioScheduler: Scheduler,
        packageManager: PackageManager,
        loggerFactory: LoggerFactory,
        packageClassifier: PackageClassifier,
        broadcastReceiverWrap: BroadcastReceiverWrap,
        @LaunchesActivityPackage launchesActivityPackage: Package
    ) = PackagesManager(
        loggerFactory = loggerFactory,
        ioScheduler = ioScheduler,
        packageManager = packageManager,
        packageClassifier = packageClassifier,
        broadcastReceiverWrap = broadcastReceiverWrap,
        launchesPackage = launchesActivityPackage
    )
}