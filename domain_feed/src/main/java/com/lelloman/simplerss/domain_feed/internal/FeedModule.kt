package com.lelloman.simplerss.domain_feed.internal

import com.lelloman.simplerss.domain_feed.FeedRepository
import com.lelloman.simplerss.domain_feed.FeedSourceOperationsProducer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.ElementsIntoSet

@Module
@InstallIn(SingletonComponent::class)
internal object FeedModule {

    @Provides
    @ElementsIntoSet
    fun provideFeedSourceOperationProducers(): Set<FeedSourceOperationsProducer> = HashSet()

    @Provides
    fun provideFeedRepository(
        feedSourceOperationsProducers: Set<@JvmSuppressWildcards FeedSourceOperationsProducer>
    ): FeedRepository = FeedRepositoryImpl(feedSourceOperationsProducers)
}