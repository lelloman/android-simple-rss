package com.lelloman.read.core.navigation

import com.lelloman.read.ui.articles.view.ArticleActivity
import com.lelloman.read.ui.articles.view.ArticlesListActivity
import com.lelloman.read.ui.settings.view.SettingsActivity
import com.lelloman.read.ui.sources.view.AddSourceActivity
import com.lelloman.read.ui.sources.view.SourceActivity
import com.lelloman.read.ui.sources.view.SourcesListActivity
import com.lelloman.read.ui.discover.view.AddFoundFeedsConfirmationDialogFragment
import com.lelloman.read.ui.discover.view.DiscoverUrlActivity
import com.lelloman.read.ui.discover.view.FoundFeedListActivity
import com.lelloman.read.ui.walkthrough.view.WalkthroughActivity
import kotlin.reflect.KClass

enum class NavigationScreen(val clazz: KClass<*>) {
    SOURCES_LIST(SourcesListActivity::class),
    ADD_SOURCE(AddSourceActivity::class),
    SOURCE(SourceActivity::class),
    ARTICLE(ArticleActivity::class),
    SETTINGS(SettingsActivity::class),
    WALKTHROUGH(WalkthroughActivity::class),
    ARTICLES_LIST(ArticlesListActivity::class),
    FOUND_FEED_LIST(FoundFeedListActivity::class),
    ADD_FOUND_FEEDS_CONFIRMATION(AddFoundFeedsConfirmationDialogFragment::class),
    DISCOVER_URL(DiscoverUrlActivity::class);
}