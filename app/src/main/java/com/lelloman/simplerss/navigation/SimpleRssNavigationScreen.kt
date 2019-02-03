package com.lelloman.simplerss.navigation

import com.lelloman.common.navigation.DeepLink
import com.lelloman.common.navigation.DeepLinkStartable
import com.lelloman.common.navigation.NavigationScreen

/**
 * Cannot change the names, [DeepLink] uses the names and if the values are changed in this enum
 * then older deep links might not work.
 */
enum class SimpleRssNavigationScreen(
    override var deepLinkStartable: DeepLinkStartable
) : NavigationScreen {
    ADD_FOUND_FEEDS_CONFIRMATION(com.lelloman.simplerss.ui.discover.view.AddFoundFeedsConfirmationDialogFragment.deepLinkStartable),
    ADD_SOURCE(com.lelloman.simplerss.ui.sources.view.AddSourceActivity.deepLinkStartable),
    ARTICLE(com.lelloman.simplerss.ui.articles.view.ArticleActivity.deepLinkStartable),
    ARTICLES_LIST(com.lelloman.simplerss.ui.articles.view.ArticlesListActivity.deepLinkStartable),
    DISCOVER_URL(com.lelloman.simplerss.ui.discover.view.DiscoverUrlActivity.deepLinkStartable),
    FOUND_FEED_LIST(com.lelloman.simplerss.ui.discover.view.FoundFeedListActivity.deepLinkStartable),
    SETTINGS(com.lelloman.simplerss.ui.settings.view.SettingsActivity.deepLinkStartable),
    SOURCES_LIST(com.lelloman.simplerss.ui.sources.view.SourcesListActivity.deepLinkStartable),
    WALKTHROUGH(com.lelloman.simplerss.ui.walkthrough.view.WalkthroughActivity.deepLinkStartable);

    companion object {

        const val ARG_SOURCE_NAME = "SourceName"
        const val ARG_SOURCE_URL = "SourceUrl"

        const val ARG_URL = "Url"

        const val ARG_FOUND_FEEDS = "FoundFeeds"
    }
}