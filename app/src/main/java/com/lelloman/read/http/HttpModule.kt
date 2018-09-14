package com.lelloman.read.http

import com.lelloman.common.utils.TimeProvider
import com.lelloman.read.core.logger.LoggerFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
open class HttpModule {

    @Singleton
    @Provides
    open fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient
            .Builder()
            .build()

    @Singleton
    @Provides
    open fun provideHttpClient(
        okHttpClient: OkHttpClient,
        loggerFactory: LoggerFactory,
        timeProvider: TimeProvider
    ): HttpClient = HttpClientImpl(
        okHttpClient = okHttpClient,
        loggerFactory = loggerFactory,
        timeProvider = timeProvider
    )
}