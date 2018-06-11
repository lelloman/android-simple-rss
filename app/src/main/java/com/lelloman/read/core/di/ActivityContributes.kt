package com.lelloman.read.core.di

import com.lelloman.read.articleslist.view.ArticlesListActivity
import com.lelloman.read.core.InjectableActivity
import com.lelloman.read.sources.view.AddSourceActivity
import com.lelloman.read.sources.view.SourcesListActivity
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
}