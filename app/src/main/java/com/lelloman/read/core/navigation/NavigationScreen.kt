package com.lelloman.read.core.navigation

import com.lelloman.read.ui.articles.view.ArticleActivity
import com.lelloman.read.ui.articles.view.ArticlesListActivity
import com.lelloman.read.ui.discover.view.AddFoundFeedsConfirmationDialogFragment
import com.lelloman.read.ui.discover.view.DiscoverUrlActivity
import com.lelloman.read.ui.discover.view.FoundFeedListActivity
import com.lelloman.read.ui.settings.view.SettingsActivity
import com.lelloman.read.ui.sources.view.AddSourceActivity
import com.lelloman.read.ui.sources.view.SourceActivity
import com.lelloman.read.ui.sources.view.SourcesListActivity
import com.lelloman.read.ui.walkthrough.view.WalkthroughActivity
import kotlin.reflect.KClass

/**
 * Cannot change the names, [DeepLink] uses the names and if the values are changed in this enum
 * then older deep links might not work.
 */
enum class NavigationScreen(val clazz: KClass<*>, deepLinkStartable: DeepLinkStartable? = null) {
    ADD_FOUND_FEEDS_CONFIRMATION(AddFoundFeedsConfirmationDialogFragment::class),
    ADD_SOURCE(AddSourceActivity::class, deepLinkStartable = AddSourceActivity.deepLinkStartable),
    ARTICLE(ArticleActivity::class),
    ARTICLES_LIST(ArticlesListActivity::class, deepLinkStartable = ArticlesListActivity.deepLinkStartable),
    DISCOVER_URL(DiscoverUrlActivity::class, deepLinkStartable = DiscoverUrlActivity.deepLinkStartable),
    FOUND_FEED_LIST(FoundFeedListActivity::class),
    SETTINGS(SettingsActivity::class, deepLinkStartable = SettingsActivity.deepLinkStartable),
    SOURCE(SourceActivity::class),
    SOURCES_LIST(SourcesListActivity::class, deepLinkStartable = SourcesListActivity.deepLinkStartable),
    WALKTHROUGH(WalkthroughActivity::class);

    var deepLinkStartable: DeepLinkStartable? = deepLinkStartable
        internal set

    companion object {

        const val ARG_SOURCE_NAME = "SourceName"
        const val ARG_SOURCE_URL = "SourceUrl"

        private val namesMap = NavigationScreen
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