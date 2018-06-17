package com.lelloman.read.http

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
    open fun provideHttpClient(okHttpClient: OkHttpClient): HttpClient =
        HttpClientImpl(okHttpClient)
}