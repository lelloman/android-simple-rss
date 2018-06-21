package com.lelloman.read.core.di

import com.lelloman.read.core.view.InjectableActivity
import com.lelloman.read.feed.FeedRefreshBroadcastReceiver
import com.lelloman.read.ui.articles.view.ArticleActivity
import com.lelloman.read.ui.articles.view.ArticlesListActivity
import com.lelloman.read.ui.settings.view.SettingsActivity
import com.lelloman.read.ui.sources.view.AddSourceActivity
import com.lelloman.read.ui.sources.view.SourceActivity
import com.lelloman.read.ui.sources.view.SourcesListActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

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
}