package com.lelloman.read.core.di

import com.lelloman.read.articleslist.view.ArticleListActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityContributes {

    @ContributesAndroidInjector
    fun articlesListActivity(): ArticleListActivity
}