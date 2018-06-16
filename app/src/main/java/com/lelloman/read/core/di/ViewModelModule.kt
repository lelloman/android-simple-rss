package com.lelloman.read.core.di

import com.lelloman.read.core.ResourceProvider
import com.lelloman.read.core.di.qualifiers.IoScheduler
import com.lelloman.read.core.di.qualifiers.UiScheduler
import com.lelloman.read.ui.articleslist.repository.ArticlesRepository
import com.lelloman.read.ui.articleslist.viewmodel.ArticlesListViewModel
import com.lelloman.read.ui.articleslist.viewmodel.ArticlesListViewModelImpl
import com.lelloman.read.ui.sources.repository.SourcesRepository
import com.lelloman.read.ui.sources.viewmodel.AddSourceViewModel
import com.lelloman.read.ui.sources.viewmodel.AddSourceViewModelImpl
import com.lelloman.read.ui.sources.viewmodel.SourcesListViewModel
import com.lelloman.read.ui.sources.viewmodel.SourcesListViewModelImpl
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler

@Module
open class ViewModelModule {

    @Provides
    open fun provideArticlesListViewModel(
        @IoScheduler ioScheduler: Scheduler,
        @UiScheduler uiScheduler: Scheduler,
        articlesRepository: ArticlesRepository,
        resourceProvider: ResourceProvider
    ): ArticlesListViewModel = ArticlesListViewModelImpl(
        ioScheduler = ioScheduler,
        uiScheduler = uiScheduler,
        articlesRepository = articlesRepository,
        resourceProvider = resourceProvider
    )

    @Provides
    open fun provideSourcesListViewModel(
        @IoScheduler ioScheduler: Scheduler,
        @UiScheduler uiScheduler: Scheduler,
        sourcesRepository: SourcesRepository,
        resourceProvider: ResourceProvider
    ): SourcesListViewModel = SourcesListViewModelImpl(
        ioScheduler = ioScheduler,
        uiScheduler = uiScheduler,
        sourcesRepository = sourcesRepository,
        resourceProvider = resourceProvider
    )

    @Provides
    open fun provideAddSourceViewModel(
        resourceProvider: ResourceProvider
    ): AddSourceViewModel = AddSourceViewModelImpl(resourceProvider)
}