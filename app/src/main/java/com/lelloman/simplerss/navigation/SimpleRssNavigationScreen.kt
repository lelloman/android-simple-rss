package com.lelloman.simplerss.navigation

import com.lelloman.common.navigation.DeepLink
import com.lelloman.common.navigation.DeepLinkStartable
import com.lelloman.common.navigation.NavigationScreen
import com.lelloman.simplerss.ui.articles.view.ArticleActivity
import com.lelloman.simplerss.ui.articles.view.ArticlesListActivity
import com.lelloman.simplerss.ui.discover.view.AddFoundFeedsConfirmationDialogFragment
import com.lelloman.simplerss.ui.discover.view.DiscoverUrlActivity
import com.lelloman.simplerss.ui.discover.view.FoundFeedListActivity
import com.lelloman.simplerss.ui.settings.view.ClearDataConfirmationDialogFragment
import com.lelloman.simplerss.ui.settings.view.SettingsActivity
import com.lelloman.simplerss.ui.sources.view.AddSourceActivity
import com.lelloman.simplerss.ui.sources.view.SourcesListActivity
import com.lelloman.simplerss.ui.walkthrough.view.WalkthroughActivity

/**
 * Cannot change the names, [DeepLink] uses the names and if the values are changed in this enum
 * then older deep links might not work.
 */
enum class SimpleRssNavigationScreen(
    override var deepLinkStartable: DeepLinkStartable
) : NavigationScreen {
    ADD_FOUND_FEEDS_CONFIRMATION(AddFoundFeedsConfirmationDialogFragment.deepLinkStartable),
    ADD_SOURCE(AddSourceActivity.deepLinkStartable),
    ARTICLE(ArticleActivity.deepLinkStartable),
    ARTICLES_LIST(ArticlesListActivity.deepLinkStartable),
    CLEAR_DATA_CONFIRMATION(ClearDataConfirmationDialogFragment.deepLinkStartable),
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