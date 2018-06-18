package com.lelloman.read.core.di

import com.lelloman.read.core.view.InjectableActivity
import com.lelloman.read.feed.FeedRefreshBroadcastReceiver
import com.lelloman.read.ui.articleslist.view.ArticlesListActivity
import com.lelloman.read.ui.sources.view.AddSourceActivity
import com.lelloman.read.ui.sources.view.SourcesListActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityContributes {

    @ContributesAndroidInjector
    fun contributeBaseActivity(): InjectableActivity

    @ContributesAndroidInjector
    fun contributeArticlesListActivity(): ArticlesListActivity

    @ContributesAndroidInjector
    fun contributeSourcesListActivity(): SourcesListActivity

    @ContributesAndroidInjector
    fun contributeAddSourceActivity(): AddSourceActivity

    @ContributesAndroidInjector
    fun contributeFeedRefreshReceiver(): FeedRefreshBroadcastReceiver
}