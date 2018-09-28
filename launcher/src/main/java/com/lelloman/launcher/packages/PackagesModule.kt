package com.lelloman.launcher.packages

import android.content.Context
import com.lelloman.common.di.qualifiers.IoScheduler
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler

@Module
class PackagesModule {

    @Provides
    fun providePackagesManager(
        @IoScheduler ioScheduler: Scheduler,
        context: Context
    ) = PackagesManager(
        context = context,
        ioScheduler = ioScheduler
    )
}