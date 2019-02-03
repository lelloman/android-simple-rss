package com.lelloman.simplerss.http

import com.lelloman.common.logger.LoggerFactory
import com.lelloman.common.utils.TimeProvider
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import java.util.concurrent.Executors
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

    @Singleton
    @Provides
    @HttpPoolScheduler
    open fun provideHttpPoolScheduler(): Scheduler = Executors
        .newFixedThreadPool(5)
        .let(Schedulers::from)
}