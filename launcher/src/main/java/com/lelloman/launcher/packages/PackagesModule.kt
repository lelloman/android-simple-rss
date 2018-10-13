package com.lelloman.launcher.packages

import android.content.Context
import android.content.pm.PackageManager
import com.lelloman.common.di.qualifiers.IoScheduler
import com.lelloman.common.di.qualifiers.NewThreadScheduler
import com.lelloman.common.view.BroadcastReceiverWrap
import com.lelloman.common.view.ResourceProvider
import com.lelloman.launcher.R
import com.lelloman.launcher.di.qualifier.LaunchesActivityPackage
import com.lelloman.launcher.di.qualifier.MainActivityPackage
import com.lelloman.launcher.logger.LauncherLoggerFactory
import com.lelloman.launcher.persistence.db.ClassifiedIdentifierDao
import com.lelloman.launcher.ui.launches.view.LaunchesActivity
import com.lelloman.launcher.ui.main.view.MainActivity
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import javax.inject.Singleton

@Module
class PackagesModule {

    @Provides
    fun providePackageLaunchExporter(
        context: Context
    ) = PackageLaunchesExporter(
        context = context
    )

    @Provides
    @LaunchesActivityPackage
    fun provideLaunchesActivityPackage(
        context: Context,
        resourceProvider: ResourceProvider
    ) = Package(
        id = -1,
        label = resourceProvider.getString(R.string.launches_activity_label),
        packageName = context.packageName,
        activityName = LaunchesActivity::class.java.name,
        drawable = resourceProvider.getDrawable(R.mipmap.ic_launcher)
    )

    @Provides
    @MainActivityPackage
    fun provideMainActivityPackage(
        context: Context,
        resourceProvider: ResourceProvider
    ) = Package(
        id = -2,
        label = "",
        packageName = context.packageName,
        activityName = MainActivity::class.java.name,
        drawable = resourceProvider.getDrawable(R.mipmap.ic_launcher)
    )

    @Provides
    @Singleton
    fun providePackagesManager(
        @IoScheduler ioScheduler: Scheduler,
        @NewThreadScheduler newThreadScheduler: Scheduler,
        packageManager: PackageManager,
        loggerFactory: LauncherLoggerFactory,
        broadcastReceiverWrap: BroadcastReceiverWrap,
        resourceProvider: ResourceProvider,
        @LaunchesActivityPackage launchesActivityPackage: Package,
        @MainActivityPackage mainActivityPackage: Package,
        classifiedPackageIdentifierDao: ClassifiedIdentifierDao
    ) = PackagesManager(
        loggerFactory = loggerFactory,
        newThreadScheduler = newThreadScheduler,
        ioScheduler = ioScheduler,
        packageManager = packageManager,
        classifiedIdentifierDao = classifiedPackageIdentifierDao,
        broadcastReceiverWrap = broadcastReceiverWrap,
        launchesPackage = launchesActivityPackage,
        mainPackage = mainActivityPackage,
        resourceProvider = resourceProvider
    )
}