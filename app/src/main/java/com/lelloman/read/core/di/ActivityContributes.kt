package com.lelloman.read.core.di

import com.lelloman.read.articleslist.view.ArticlesListActivity
import com.lelloman.read.sources.view.SourcesListActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityContributes {

    @ContributesAndroidInjector
    fun articlesListActivity(): ArticlesListActivity

    @ContributesAndroidInjector
    fun sourcesListActivity(): SourcesListActivity
}