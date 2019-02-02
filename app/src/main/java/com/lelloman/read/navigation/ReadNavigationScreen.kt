package com.lelloman.read.navigation

import com.lelloman.common.navigation.DeepLink
import com.lelloman.common.navigation.DeepLinkStartable
import com.lelloman.common.navigation.NavigationScreen
import com.lelloman.read.ui.articles.view.ArticleActivity
import com.lelloman.read.ui.articles.view.ArticlesListActivity
import com.lelloman.read.ui.discover.view.AddFoundFeedsConfirmationDialogFragment
import com.lelloman.read.ui.discover.view.DiscoverUrlActivity
import com.lelloman.read.ui.discover.view.FoundFeedListActivity
import com.lelloman.read.ui.settings.view.SettingsActivity
import com.lelloman.read.ui.sources.view.AddSourceActivity
import com.lelloman.read.ui.sources.view.SourcesListActivity
import com.lelloman.read.ui.walkthrough.view.WalkthroughActivity

/**
 * Cannot change the names, [DeepLink] uses the names and if the values are changed in this enum
 * then older deep links might not work.
 */
enum class ReadNavigationScreen(
    override var deepLinkStartable: DeepLinkStartable
) : NavigationScreen {
    ADD_FOUND_FEEDS_CONFIRMATION(AddFoundFeedsConfirmationDialogFragment.deepLinkStartable),
    ADD_SOURCE(AddSourceActivity.deepLinkStartable),
    ARTICLE(ArticleActivity.deepLinkStartable),
    ARTICLES_LIST(ArticlesListActivity.deepLinkStartable),
    DISCOVER_URL(DiscoverUrlActivity.deepLinkStartable),
    FOUND_FEED_LIST(FoundFeedListActivity.deepLinkStartable),
    SETTINGS(SettingsActivity.deepLinkStartable),
    SOURCES_LIST(SourcesListActivity.deepLinkStartable),
    WALKTHROUGH(WalkthroughActivity.deepLinkStartable);

    companion object {

        const val ARG_SOURCE_NAME = "SourceName"
        const val ARG_SOURCE_URL = "SourceUrl"

        const val ARG_URL = "Url"

        const val ARG_FOUND_FEEDS = "FoundFeeds"
    }
}