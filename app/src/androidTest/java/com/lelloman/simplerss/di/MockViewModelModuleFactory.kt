package com.lelloman.simplerss.di

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
import org.mockito.Mockito.mock

class MockViewModelModuleFactory : ViewModelModuleFactory() {

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

    override fun provideSourcesListViewModel(
        sourcesRepository: SourcesRepository,
        articlesRepository: ArticlesRepository,
        dependencies: BaseViewModel.Dependencies
    ): SourcesListViewModel = sourcesListViewModel

    override fun provideAddSourceViewModel(
        sourcesRepository: SourcesRepository,
        dependencies: BaseViewModel.Dependencies,
        feedFetcher: FeedFetcher,
        urlValidator: UrlValidator
    ): AddSourceViewModel = addSourceViewModel
}