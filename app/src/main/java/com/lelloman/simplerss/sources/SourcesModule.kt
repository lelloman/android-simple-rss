package com.lelloman.simplerss.sources

import com.lelloman.simplerss.domain_local_sources.LocalSourcesRepository
import com.lelloman.simplerss.domain_remote_sources.RemoteSourcesRepository
import com.lelloman.simplerss.navigation.NavigationEventProcessor
import com.lelloman.simplerss.ui_source.list.SourcesInteractor
import com.lelloman.simplerss.ui_source.local.LocalSourceInteractor
import com.lelloman.simplerss.ui_source.remote.RemoteSourceInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object SourcesModule {

    @Provides
    fun provideUserSourcesAdapter(
        remoteSourcesRepository: RemoteSourcesRepository,
        localSourcesRepository: LocalSourcesRepository
    ) = UserSourcesAdapter(
        localSourcesRepository = localSourcesRepository,
        remoteSourcesRepository = remoteSourcesRepository
    )

    @Provides
    fun provideSourcesInteractor(
        userSourcesAdapter: UserSourcesAdapter,
        navigationEventProcessor: NavigationEventProcessor
    ): SourcesInteractor = SourcesInteractorImpl(
        userSourcesAdapter = userSourcesAdapter,
        navigationEventProcessor = navigationEventProcessor
    )

    @Provides
    fun provideLocalSourceInteractor(
        localSourcesRepository: LocalSourcesRepository
    ): LocalSourceInteractor = LocalSourceInteractorImpl(
        localSourcesRepository = localSourcesRepository
    )

    @Provides
    fun provideRemoteSourceInteractor(): RemoteSourceInteractor = RemoteSourceInteractorImpl()
}