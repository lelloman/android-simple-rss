package com.lelloman.read.di

import com.lelloman.common.utils.UrlValidator
import com.lelloman.common.view.ResourceProvider
import com.lelloman.read.core.di.ViewModelModule
import com.lelloman.read.core.di.qualifiers.IoScheduler
import com.lelloman.read.core.di.qualifiers.UiScheduler
import com.lelloman.read.core.logger.LoggerFactory
import com.lelloman.read.feed.fetcher.FeedFetcher
import com.lelloman.read.persistence.settings.AppSettings
import com.lelloman.read.ui.articles.viewmodel.ArticlesListViewModel
import com.lelloman.read.ui.common.repository.ArticlesRepository
import com.lelloman.read.ui.common.repository.DiscoverRepository
import com.lelloman.read.ui.common.repository.SourcesRepository
import com.lelloman.read.ui.sources.viewmodel.AddSourceViewModel
import com.lelloman.read.ui.sources.viewmodel.SourcesListViewModel
import dagger.Provides
import io.reactivex.Scheduler
import org.mockito.Mockito.mock

class MockViewModelModule : ViewModelModule() {

    val articlesListViewModel: ArticlesListViewModel = mock(ArticlesListViewModel::class.java)
    private val sourcesListViewModel: SourcesListViewModel = mock(SourcesListViewModel::class.java)
    private val addSourceViewModel: AddSourceViewModel = mock(AddSourceViewModel::class.java)

    override fun provideArticlesListViewModel(
        @IoScheduler ioScheduler: Scheduler,
        @UiScheduler uiScheduler: Scheduler,
        articlesRepository: ArticlesRepository,
        resourceProvider: ResourceProvider,
        sourcesRepository: SourcesRepository,
        discoverRepository: DiscoverRepository,
        appSettings: AppSettings
    ): ArticlesListViewModel = articlesListViewModel

    @Provides
    override fun provideSourcesListViewModel(
        @IoScheduler ioScheduler: Scheduler,
        @UiScheduler uiScheduler: Scheduler,
        sourcesRepository: SourcesRepository,
        articlesRepository: ArticlesRepository,
        resourceProvider: ResourceProvider
    ): SourcesListViewModel = sourcesListViewModel

    @Provides
    override fun provideAddSourceViewModel(
        @IoScheduler ioScheduler: Scheduler,
        @UiScheduler uiScheduler: Scheduler,
        sourcesRepository: SourcesRepository,
        resourceProvider: ResourceProvider,
        feedFetcher: FeedFetcher,
        loggerFactory: LoggerFactory,
        urlValidator: UrlValidator
    ): AddSourceViewModel = addSourceViewModel
}