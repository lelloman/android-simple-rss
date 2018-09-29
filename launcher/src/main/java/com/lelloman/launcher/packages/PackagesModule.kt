package com.lelloman.launcher.packages

import android.content.Context
import android.content.pm.PackageManager
import com.lelloman.common.di.qualifiers.IoScheduler
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler

@Module
class PackagesModule {

    @Provides
    fun providePackagesManager(
        @IoScheduler ioScheduler: Scheduler,
        context: Context,
        packageManager: PackageManager
    ) = PackagesManager(
        context = context,
        ioScheduler = ioScheduler,
        packageManager = packageManager
    )
}