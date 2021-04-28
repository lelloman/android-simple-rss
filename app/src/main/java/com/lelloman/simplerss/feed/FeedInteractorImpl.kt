package com.lelloman.simplerss.feed

import com.lelloman.simplerss.R
import com.lelloman.simplerss.domain_feed.FeedRepository
import com.lelloman.simplerss.navigation.NavigationEventProcessor
import com.lelloman.simplerss.ui_feed.model.FeedInteractor
import io.reactivex.rxjava3.core.Single

private typealias FeedRepositoryFeedItem = com.lelloman.simplerss.domain_feed.FeedItem

class FeedInteractorImpl(
    private val navigationEventProcessor: NavigationEventProcessor,
    private val feedRepository: FeedRepository
) : FeedInteractor {

    override fun loadFeed(): Single<List<FeedInteractor.FeedItem>> = feedRepository.observeFeed().firstOrError()
        .map { items -> items.map(::FeedItemWrapper) }

    override fun goToAbout() = navigationEventProcessor {
        it.navigate(R.id.action_feedFragment_to_aboutFragment)
    }

    override fun goToSettings() = navigationEventProcessor {
        it.navigate(R.id.action_feedFragment_to_settingsFragment)
    }

    override fun goToSources() = navigationEventProcessor {
        it.navigate(R.id.action_feedFragment_to_sourcesFragment)
    }

    private class FeedItemWrapper(private val feedItem: FeedRepositoryFeedItem) :
        FeedRepositoryFeedItem by feedItem,
        FeedInteractor.FeedItem
}