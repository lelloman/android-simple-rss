package com.lelloman.simplerss.di

import com.lelloman.common.view.InjectableActivity
import com.lelloman.simplerss.feed.FeedRefreshBroadcastReceiver
import com.lelloman.simplerss.ui.articles.view.ArticleActivity
import com.lelloman.simplerss.ui.articles.view.ArticlesListActivity
import com.lelloman.simplerss.ui.debug.view.DebugActivity
import com.lelloman.simplerss.ui.discover.view.DiscoverUrlActivity
import com.lelloman.simplerss.ui.discover.view.FoundFeedListActivity
import com.lelloman.simplerss.ui.launcher.view.LauncherActivity
import com.lelloman.simplerss.ui.settings.view.SettingsActivity
import com.lelloman.simplerss.ui.sources.view.AddSourceActivity
import com.lelloman.simplerss.ui.sources.view.SourceActivity
import com.lelloman.simplerss.ui.sources.view.SourcesListActivity
import com.lelloman.simplerss.ui.walkthrough.view.WalkthroughActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
interface AndroidContributes {

    @ContributesAndroidInjector
    fun contributeBaseActivity(): InjectableActivity

    @ContributesAndroidInjector
    fun contributeArticlesListActivity(): ArticlesListActivity

    @ContributesAndroidInjector
    fun contributeSourcesListActivity(): SourcesListActivity

    @ContributesAndroidInjector
    fun contributeAddSourceActivity(): AddSourceActivity

    @ContributesAndroidInjector
    fun contributeSourceActivity(): SourceActivity

    @ContributesAndroidInjector
    fun contributeArticleActivity(): ArticleActivity

    @ContributesAndroidInjector
    fun contributeFeedRefreshReceiver(): FeedRefreshBroadcastReceiver

    @ContributesAndroidInjector
    fun contributeSettingsActivity(): SettingsActivity

    @ContributesAndroidInjector
    fun contributeWalkthroughActivity(): WalkthroughActivity

    @ContributesAndroidInjector
    fun contributeLauncherActivity(): LauncherActivity

    @ContributesAndroidInjector
    fun contributeFoundFeedListActivity(): FoundFeedListActivity

    @ContributesAndroidInjector
    fun contributeDiscoverUrlActivity(): DiscoverUrlActivity

    @ContributesAndroidInjector
    fun contributeDebugActivity(): DebugActivity
}