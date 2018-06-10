package com.lelloman.read.core.di

import com.lelloman.read.articleslist.repository.ArticlesRepository
import com.lelloman.read.articleslist.viewmodel.ArticlesListViewModel
import com.lelloman.read.articleslist.viewmodel.ArticlesListViewModelImpl
import com.lelloman.read.core.di.qualifiers.IoScheduler
import com.lelloman.read.core.di.qualifiers.UiScheduler
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler

@Module
open class ViewModelModule {

    @Provides
    open fun provideArticlesListViewModel(
        @IoScheduler ioScheduler: Scheduler,
        @UiScheduler uiScheduler: Scheduler,
        articlesRepository: ArticlesRepository
    ): ArticlesListViewModel = ArticlesListViewModelImpl(
        ioScheduler,
        uiScheduler,
        articlesRepository
    )
}