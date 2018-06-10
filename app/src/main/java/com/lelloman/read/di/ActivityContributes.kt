package com.lelloman.read.di

import com.lelloman.read.articleslist.view.ArticleListActivity
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityContributes {

    @ContributesAndroidInjector
    fun articlesListActivity(): ArticleListActivity
}