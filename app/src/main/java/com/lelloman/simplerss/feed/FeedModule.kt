package com.lelloman.simplerss.feed

import com.lelloman.simplerss.domain_feed.FeedItem
import com.lelloman.simplerss.domain_feed.FeedRepository
import com.lelloman.simplerss.domain_feed.FeedSource
import com.lelloman.simplerss.domain_feed.FeedSourceOperationsProducer
import com.lelloman.simplerss.navigation.NavigationEventProcessor
import com.lelloman.simplerss.ui_feed.model.FeedInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

@Module
@InstallIn(ViewModelComponent::class)
object UiFeedModule {

    @Provides
    @ViewModelScoped
    fun provideFeedInteractor(
        navigationEventProcessor: NavigationEventProcessor,
        feedRepository: FeedRepository
    ): FeedInteractor = FeedInteractorImpl(navigationEventProcessor, feedRepository)
}

@Module
@InstallIn(SingletonComponent::class)
object DomainFeedModule {

    @Provides
    @IntoSet
    fun providesStaticFeedSourceOperationProducer(): FeedSourceOperationsProducer {
        return FeedSourceOperationsProducer { Observable.just(listOf(FeedSource.Operation.Add(FakeFeedSource()))) }
    }

    class FakeFeedSource : FeedSource {
        override val id: String = "fake source"

        override fun observeItems(): Observable<List<FeedItem>> {
            return Observable.just(Array(50) { FakeFeedItem(it, id) }.toList())
        }

        override fun refresh(): Completable {
            return Completable.timer(2, TimeUnit.SECONDS)
        }

        class FakeFeedItem(
            it: Int,
            override val sourceId: String
        ) : FeedItem {
            override val id: String = "$it"
            override val title: String = "title $it"
            override val subtitle: String = "subtitle $it"
            override val content: String = "content $it"
            override val link: String = "link $it"
            override val imageUrl: String? = ""
            override val time: Long = 0
            override val sourceName: String = "source $it"
            override val icon: ByteArray? = null
        }
    }
}