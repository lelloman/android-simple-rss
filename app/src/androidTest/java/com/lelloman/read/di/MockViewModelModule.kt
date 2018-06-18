package com.lelloman.read.di

import com.lelloman.read.core.ResourceProvider
import com.lelloman.read.core.di.ViewModelModule
import com.lelloman.read.core.di.qualifiers.IoScheduler
import com.lelloman.read.core.di.qualifiers.UiScheduler
import com.lelloman.read.ui.articleslist.repository.ArticlesRepository
import com.lelloman.read.ui.articleslist.viewmodel.ArticlesListViewModel
import com.lelloman.read.ui.sources.repository.SourcesRepository
import com.lelloman.read.ui.sources.viewmodel.AddSourceViewModel
import com.lelloman.read.ui.sources.viewmodel.SourcesListViewModel
import dagger.Provides
import io.reactivex.Scheduler
import org.mockito.Mockito.mock

class MockViewModelModule : ViewModelModule() {

    val articlesListViewModel: ArticlesListViewModel = mock(ArticlesListViewModel::class.java)
    val sourcesListViewModel: SourcesListViewModel = mock(SourcesListViewModel::class.java)
    val addSourceViewModel: AddSourceViewModel = mock(AddSourceViewModel::class.java)

    override fun provideArticlesListViewModel(
        @IoScheduler ioScheduler: Scheduler,
        @UiScheduler uiScheduler: Scheduler,
        articlesRepository: ArticlesRepository,
        resourceProvider: ResourceProvider
    ): ArticlesListViewModel = articlesListViewModel

    @Provides
    override fun provideSourcesListViewModel(
        @IoScheduler ioScheduler: Scheduler,
        @UiScheduler uiScheduler: Scheduler,
        sourcesRepository: SourcesRepository,
        resourceProvider: ResourceProvider
    ): SourcesListViewModel = sourcesListViewModel

    @Provides
    override fun provideAddSourceViewModel(
        resourceProvider: ResourceProvider
    ): AddSourceViewModel = addSourceViewModel
}