package com.lelloman.simplerss.di

import com.lelloman.common.logger.LoggerFactory
import com.lelloman.common.utils.UrlValidator
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.simplerss.feed.fetcher.FeedFetcher
import com.lelloman.simplerss.persistence.settings.AppSettings
import com.lelloman.simplerss.ui.articles.viewmodel.ArticlesListViewModel
import com.lelloman.simplerss.ui.common.repository.ArticlesRepository
import com.lelloman.simplerss.ui.common.repository.DiscoverRepository
import com.lelloman.simplerss.ui.common.repository.SourcesRepository
import com.lelloman.simplerss.ui.sources.viewmodel.AddSourceViewModel
import com.lelloman.simplerss.ui.sources.viewmodel.SourcesListViewModel
import dagger.Provides
import org.mockito.Mockito.mock

class MockViewModelModule : ViewModelModule() {

    val articlesListViewModel: ArticlesListViewModel = mock(ArticlesListViewModel::class.java)
    val sourcesListViewModel: SourcesListViewModel = mock(SourcesListViewModel::class.java)
    private val addSourceViewModel: AddSourceViewModel = mock(AddSourceViewModel::class.java)

    override fun provideArticlesListViewModel(
        articlesRepository: ArticlesRepository,
        sourcesRepository: SourcesRepository,
        discoverRepository: DiscoverRepository,
        dependencies: BaseViewModel.Dependencies,
        appSettings: AppSettings
    ): ArticlesListViewModel = articlesListViewModel

    @Provides
    override fun provideSourcesListViewModel(
        sourcesRepository: SourcesRepository,
        articlesRepository: ArticlesRepository,
        dependencies: BaseViewModel.Dependencies
    ): SourcesListViewModel = sourcesListViewModel

    @Provides
    override fun provideAddSourceViewModel(
        sourcesRepository: SourcesRepository,
        dependencies: BaseViewModel.Dependencies,
        feedFetcher: FeedFetcher,
        loggerFactory: LoggerFactory,
        urlValidator: UrlValidator
    ): AddSourceViewModel = addSourceViewModel
}