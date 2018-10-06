package com.lelloman.read.navigation

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
import kotlin.reflect.KClass
import com.lelloman.common.navigation.DeepLink

/**
 * Cannot change the names, [DeepLink] uses the names and if the values are changed in this enum
 * then older deep links might not work.
 */
enum class ReadNavigationScreen(
    override val clazz: KClass<*>,
    override var deepLinkStartable: DeepLinkStartable? = null
) : NavigationScreen {
    ADD_FOUND_FEEDS_CONFIRMATION(AddFoundFeedsConfirmationDialogFragment::class, deepLinkStartable = AddFoundFeedsConfirmationDialogFragment.deepLinkStartable),
    ADD_SOURCE(AddSourceActivity::class, deepLinkStartable = AddSourceActivity.deepLinkStartable),
    ARTICLE(ArticleActivity::class, deepLinkStartable = ArticleActivity.deepLinkStartable),
    ARTICLES_LIST(ArticlesListActivity::class, deepLinkStartable = ArticlesListActivity.deepLinkStartable),
    DISCOVER_URL(DiscoverUrlActivity::class, deepLinkStartable = DiscoverUrlActivity.deepLinkStartable),
    FOUND_FEED_LIST(FoundFeedListActivity::class, deepLinkStartable = FoundFeedListActivity.deepLinkStartable),
    SETTINGS(SettingsActivity::class, deepLinkStartable = SettingsActivity.deepLinkStartable),
    SOURCES_LIST(SourcesListActivity::class, deepLinkStartable = SourcesListActivity.deepLinkStartable),
    WALKTHROUGH(WalkthroughActivity::class, deepLinkStartable = WalkthroughActivity.deepLinkStartable);

    companion object {

        const val ARG_SOURCE_NAME = "SourceName"
        const val ARG_SOURCE_URL = "SourceUrl"

        const val ARG_URL = "Url"

        const val ARG_FOUND_FEEDS = "FoundFeeds"

        private val namesMap = ReadNavigationScreen
            .values()
            .associateBy(NavigationScreen::name)

        fun fromName(name: String): NavigationScreen? =
            if (namesMap.containsKey(name)) {
                namesMap[name]!!
            } else {
                null
            }
    }
}