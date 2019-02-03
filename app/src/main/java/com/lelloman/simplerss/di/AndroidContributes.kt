package com.lelloman.simplerss.di

import com.lelloman.common.view.InjectableActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
interface AndroidContributes {

    @ContributesAndroidInjector
    fun contributeBaseActivity(): InjectableActivity

    @ContributesAndroidInjector
    fun contributeArticlesListActivity(): com.lelloman.simplerss.ui.articles.view.ArticlesListActivity

    @ContributesAndroidInjector
    fun contributeSourcesListActivity(): com.lelloman.simplerss.ui.sources.view.SourcesListActivity

    @ContributesAndroidInjector
    fun contributeAddSourceActivity(): com.lelloman.simplerss.ui.sources.view.AddSourceActivity

    @ContributesAndroidInjector
    fun contributeSourceActivity(): com.lelloman.simplerss.ui.sources.view.SourceActivity

    @ContributesAndroidInjector
    fun contributeArticleActivity(): com.lelloman.simplerss.ui.articles.view.ArticleActivity

    @ContributesAndroidInjector
    fun contributeFeedRefreshReceiver(): com.lelloman.simplerss.feed.FeedRefreshBroadcastReceiver

    @ContributesAndroidInjector
    fun contributeSettingsActivity(): com.lelloman.simplerss.ui.settings.view.SettingsActivity

    @ContributesAndroidInjector
    fun contributeWalkthroughActivity(): com.lelloman.simplerss.ui.walkthrough.view.WalkthroughActivity

    @ContributesAndroidInjector
    fun contributeLauncherActivity(): com.lelloman.simplerss.ui.launcher.view.LauncherActivity

    @ContributesAndroidInjector
    fun contributeFoundFeedListActivity(): com.lelloman.simplerss.ui.discover.view.FoundFeedListActivity

    @ContributesAndroidInjector
    fun contributeDiscoverUrlActivity(): com.lelloman.simplerss.ui.discover.view.DiscoverUrlActivity
}