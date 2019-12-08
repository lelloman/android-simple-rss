package com.lelloman.simplerss.di

import com.lelloman.common.di.KoinModuleFactory
import com.lelloman.common.utils.UrlValidator
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.simplerss.feed.fetcher.FeedFetcher
import com.lelloman.simplerss.persistence.settings.AppSettings
import com.lelloman.simplerss.ui.articles.viewmodel.ArticleViewModel
import com.lelloman.simplerss.ui.articles.viewmodel.ArticleViewModelImpl
import com.lelloman.simplerss.ui.articles.viewmodel.ArticlesListViewModel
import com.lelloman.simplerss.ui.articles.viewmodel.ArticlesListViewModelImpl
import com.lelloman.simplerss.ui.common.repository.ArticlesRepository
import com.lelloman.simplerss.ui.common.repository.DiscoverRepository
import com.lelloman.simplerss.ui.common.repository.SourcesRepository
import com.lelloman.simplerss.ui.debug.viewmodel.DebugViewModel
import com.lelloman.simplerss.ui.discover.viewmodel.DiscoverUrlViewModel
import com.lelloman.simplerss.ui.discover.viewmodel.DiscoverUrlViewModelImpl
import com.lelloman.simplerss.ui.discover.viewmodel.FoundFeedListViewModel
import com.lelloman.simplerss.ui.discover.viewmodel.FoundFeedListViewModelImpl
import com.lelloman.simplerss.ui.launcher.viewmodel.LauncherViewModel
import com.lelloman.simplerss.ui.launcher.viewmodel.LauncherViewModelImpl
import com.lelloman.simplerss.ui.settings.viewmodel.SettingsViewModel
import com.lelloman.simplerss.ui.settings.viewmodel.SettingsViewModelImpl
import com.lelloman.simplerss.ui.sources.viewmodel.*
import com.lelloman.simplerss.ui.walkthrough.viewmodel.WalkthroughViewModel
import com.lelloman.simplerss.ui.walkthrough.viewmodel.WalkthroughViewModelImpl
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

open class ViewModelModuleFactory : KoinModuleFactory {

    override fun makeKoinModule(override: Boolean) = module(override=override) {
        viewModel {
            provideArticlesListViewModel(
                articlesRepository = get(),
                sourcesRepository = get(),
                discoverRepository = get(),
                appSettings = get(),
                dependencies = get()
            )
        }
        viewModel {
            provideSourcesListViewModel(
                sourcesRepository = get(),
                articlesRepository = get(),
                dependencies = get()
            )
        }
        viewModel {
            provideAddSourceViewModel(
                dependencies = get(),
                sourcesRepository = get(),
                feedFetcher = get(),
                urlValidator = get()
            )
        }
        viewModel<SourceViewModel> {
            SourceViewModelImpl(
                dependencies = get(),
                sourcesRepository = get()
            )
        }
        viewModel<ArticleViewModel> {
            ArticleViewModelImpl(
                dependencies = get()
            )
        }
        viewModel<SettingsViewModel> {
            SettingsViewModelImpl(
                appSettings = get(),
                dependencies = get(),
                semanticTimeProvider = get(),
                appDatabase = get(),
                fileProvider = get()
            )
        }
        viewModel<WalkthroughViewModel> {
            WalkthroughViewModelImpl(
                htmlSpanner = get(),
                dependencies = get(),
                appSettings = get(),
                discoveryRepository = get(),
                urlValidator = get()
            )
        }
        viewModel<LauncherViewModel> {
            LauncherViewModelImpl(
                dependencies = get(),
                appSettings = get()
            )
        }
        viewModel<FoundFeedListViewModel> {
            FoundFeedListViewModelImpl(
                discoverRepository = get(),
                dependencies = get(),
                sourcesDao = get()
            )
        }
        viewModel<DiscoverUrlViewModel> {
            DiscoverUrlViewModelImpl(
                discoverRepository = get(),
                urlValidator = get(),
                dependencies = get()
            )
        }
        viewModel {
            DebugViewModel(
                dependencies = get(),
                appSettings = get(),
                appDatabase = get()
            )
        }
    }

    protected open fun provideArticlesListViewModel(
        articlesRepository: ArticlesRepository,
        sourcesRepository: SourcesRepository,
        discoverRepository: DiscoverRepository,
        dependencies: BaseViewModel.Dependencies,
        appSettings: AppSettings
    ): ArticlesListViewModel = ArticlesListViewModelImpl(
        articlesRepository = articlesRepository,
        sourcesRepository = sourcesRepository,
        discoverRepository = discoverRepository,
        appSettings = appSettings,
        dependencies = dependencies
    )

    protected open fun provideSourcesListViewModel(
        sourcesRepository: SourcesRepository,
        articlesRepository: ArticlesRepository,
        dependencies: BaseViewModel.Dependencies
    ): SourcesListViewModel = SourcesListViewModelImpl(
        sourcesRepository = sourcesRepository,
        articlesRepository = articlesRepository,
        dependencies = dependencies
    )

    protected open fun provideAddSourceViewModel(
        sourcesRepository: SourcesRepository,
        dependencies: BaseViewModel.Dependencies,
        feedFetcher: FeedFetcher,
        urlValidator: UrlValidator
    ): AddSourceViewModel = AddSourceViewModelImpl(
        sourcesRepository = sourcesRepository,
        dependencies = dependencies,
        feedFetcher = feedFetcher,
        urlValidator = urlValidator
    )
}
