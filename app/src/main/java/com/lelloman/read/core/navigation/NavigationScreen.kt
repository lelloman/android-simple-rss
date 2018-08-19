package com.lelloman.read.core.navigation

import com.lelloman.read.core.view.BaseActivity
import com.lelloman.read.ui.articles.view.ArticleActivity
import com.lelloman.read.ui.articles.view.ArticlesListActivity
import com.lelloman.read.ui.settings.view.SettingsActivity
import com.lelloman.read.ui.sources.view.AddSourceActivity
import com.lelloman.read.ui.sources.view.SourceActivity
import com.lelloman.read.ui.sources.view.SourcesListActivity
import com.lelloman.read.ui.walkthrough.view.WalkthroughActivity
import kotlin.reflect.KClass

enum class NavigationScreen(val clazz: KClass<out BaseActivity<*, *>>) {
    SOURCES_LIST(SourcesListActivity::class),
    ADD_SOURCE(AddSourceActivity::class),
    SOURCE(SourceActivity::class),
    ARTICLE(ArticleActivity::class),
    SETTINGS(SettingsActivity::class),
    WALKTHROUGH(WalkthroughActivity::class),
    ARTICLES_LIST(ArticlesListActivity::class);
}