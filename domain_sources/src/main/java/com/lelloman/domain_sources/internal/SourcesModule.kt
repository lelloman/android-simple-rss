package com.lelloman.domain_sources.internal

import com.lelloman.domain_sources.SourceOperationsProducer
import com.lelloman.domain_sources.SourcesRepository
import com.lelloman.simplerss.domain_feed.FeedSourceOperationsProducer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.ElementsIntoSet
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object SourcesModule {

    @Provides
    @ElementsIntoSet
    fun provideSourceOperationProducers(): Set<SourceOperationsProducer> = HashSet()

    @Provides
    @Singleton
    fun providesSourceRepositoryImpl(
        sourceOperationsProducers: Set<@JvmSuppressWildcards SourceOperationsProducer>
    ) = SourcesRepositoryImpl(sourceOperationsProducers)

    @Provides
    fun provideSourceRepository(impl: SourcesRepositoryImpl): SourcesRepository = impl

    @Provides
    @IntoSet
    fun provideFeedSourceOperationsProducer(sourcesRepositoryImpl: SourcesRepositoryImpl): FeedSourceOperationsProducer =
        sourcesRepositoryImpl
}