package com.lelloman.read.core.navigation

import com.lelloman.read.ui.articles.view.ArticleActivity
import com.lelloman.read.ui.articles.view.ArticlesListActivity
import com.lelloman.read.ui.settings.view.SettingsActivity
import com.lelloman.read.ui.sources.view.AddSourceActivity
import com.lelloman.read.ui.sources.view.SourceActivity
import com.lelloman.read.ui.sources.view.SourcesListActivity
import com.lelloman.read.ui.walkthrough.view.WalkthroughActivity
import kotlin.reflect.KFunction

enum class NavigationScreen(vararg val starters: KFunction<Unit>) {
    SOURCES_LIST(SourcesListActivity.Companion::start),
    ADD_SOURCE(AddSourceActivity.Companion::start),
    SOURCE(SourceActivity.Companion::start),
    ARTICLE(ArticleActivity.Companion::start),
    SETTINGS(SettingsActivity.Companion::start),
    WALKTHROUGH(WalkthroughActivity.Companion::start),
    ARTICLES_LIST(ArticlesListActivity.Companion::start);
}