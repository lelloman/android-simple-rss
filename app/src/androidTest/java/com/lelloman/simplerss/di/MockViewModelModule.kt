package com.lelloman.simplerss.di

import com.lelloman.common.logger.LoggerFactory
import com.lelloman.common.utils.UrlValidator
import com.lelloman.common.viewmodel.BaseViewModel
import dagger.Provides
import org.mockito.Mockito.mock

class MockViewModelModule : com.lelloman.simplerss.di.ViewModelModule() {

    val articlesListViewModel: com.lelloman.simplerss.ui.articles.viewmodel.ArticlesListViewModel = mock(com.lelloman.simplerss.ui.articles.viewmodel.ArticlesListViewModel::class.java)
    val sourcesListViewModel: com.lelloman.simplerss.ui.sources.viewmodel.SourcesListViewModel = mock(com.lelloman.simplerss.ui.sources.viewmodel.SourcesListViewModel::class.java)
    private val addSourceViewModel: com.lelloman.simplerss.ui.sources.viewmodel.AddSourceViewModel = mock(com.lelloman.simplerss.ui.sources.viewmodel.AddSourceViewModel::class.java)

    override fun provideArticlesListViewModel(
        articlesRepository: com.lelloman.simplerss.ui.common.repository.ArticlesRepository,
        sourcesRepository: com.lelloman.simplerss.ui.common.repository.SourcesRepository,
        discoverRepository: com.lelloman.simplerss.ui.common.repository.DiscoverRepository,
        dependencies: BaseViewModel.Dependencies,
        appSettings: com.lelloman.simplerss.persistence.settings.AppSettings
    ): com.lelloman.simplerss.ui.articles.viewmodel.ArticlesListViewModel = articlesListViewModel

    @Provides
    override fun provideSourcesListViewModel(
        sourcesRepository: com.lelloman.simplerss.ui.common.repository.SourcesRepository,
        articlesRepository: com.lelloman.simplerss.ui.common.repository.ArticlesRepository,
        dependencies: BaseViewModel.Dependencies
    ): com.lelloman.simplerss.ui.sources.viewmodel.SourcesListViewModel = sourcesListViewModel

    @Provides
    override fun provideAddSourceViewModel(
        sourcesRepository: com.lelloman.simplerss.ui.common.repository.SourcesRepository,
        dependencies: BaseViewModel.Dependencies,
        feedFetcher: com.lelloman.simplerss.feed.fetcher.FeedFetcher,
        loggerFactory: LoggerFactory,
        urlValidator: UrlValidator
    ): com.lelloman.simplerss.ui.sources.viewmodel.AddSourceViewModel = addSourceViewModel
}