package com.lelloman.simplerss.domain_feed.internal

import com.lelloman.simplerss.domain_feed.FeedRepository
import com.lelloman.simplerss.domain_feed.FeedSourceOperationProducer
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
    fun provideFeedSourceOperationProducers(): Set<FeedSourceOperationProducer> = HashSet()

    @Provides
    fun provideFeedRepository(
        feedSourceOperationProducers: Set<@JvmSuppressWildcards FeedSourceOperationProducer>
    ): FeedRepository = FeedRepositoryImpl(feedSourceOperationProducers)
}