package com.lelloman.simplerss.domain_remote_sources.internal

import com.lelloman.simplerss.domain_remote_sources.RemoteSourcesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object RemoteSourcesModule {

    @Provides
    @Singleton
    fun provideRemoteSourcesRepository(): RemoteSourcesRepository = RemoteSourcesRepositoryImpl()
}