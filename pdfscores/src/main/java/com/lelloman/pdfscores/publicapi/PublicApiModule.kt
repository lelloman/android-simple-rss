package com.lelloman.pdfscores.publicapi

import android.content.Context
import android.content.pm.PackageManager
import com.lelloman.common.di.qualifiers.IoScheduler
import com.lelloman.common.di.qualifiers.NewThreadScheduler
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import javax.inject.Singleton

@Module
class PublicApiModule {

    @Provides
    @Singleton
    fun providePublicPdfScoresAppsFinder(
        packageManager: PackageManager,
        @IoScheduler ioScheduler: Scheduler,
        @NewThreadScheduler newThreadScheduler: Scheduler,
        context: Context
    ): PublicPdfScoresAppsFinder = PublicPdfScoresAppsFinderImpl(
        context = context,
        newThreadScheduler = newThreadScheduler,
        packageManager = packageManager,
        ioScheduler = ioScheduler
    )
}