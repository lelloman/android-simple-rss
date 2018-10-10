package com.lelloman.launcher.logger

import android.content.Context
import com.lelloman.common.utils.TimeProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LoggerModule {

    @Provides
    @Singleton
    fun provideLoggerPrintWriter(context: Context) = LoggerPrintWriter(context)

    @Provides
    @Singleton
    fun provideLauncherLoggerFactory(
        timerProvider: TimeProvider,
        printWriter: LoggerPrintWriter
    ) = LauncherLoggerFactory(
        timeProvider = timerProvider,
        logPrintWriter = printWriter
    )
}