package com.lelloman.simplerss.di

import androidx.lifecycle.ViewModel
import com.lelloman.common.utils.UrlValidator
import com.lelloman.common.view.FileProvider
import com.lelloman.common.view.SemanticTimeProvider
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.simplerss.feed.fetcher.FeedFetcher
import com.lelloman.simplerss.html.HtmlSpanner
import com.lelloman.simplerss.persistence.db.AppDatabase
import com.lelloman.simplerss.persistence.db.SourcesDao
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
import com.lelloman.simplerss.ui.sources.viewmodel.AddSourceViewModel
import com.lelloman.simplerss.ui.sources.viewmodel.AddSourceViewModelImpl
import com.lelloman.simplerss.ui.sources.viewmodel.SourceViewModel
import com.lelloman.simplerss.ui.sources.viewmodel.SourceViewModelImpl
import com.lelloman.simplerss.ui.sources.viewmodel.SourcesListViewModel
import com.lelloman.simplerss.ui.sources.viewmodel.SourcesListViewModelImpl
import com.lelloman.simplerss.ui.walkthrough.viewmodel.WalkthroughViewModel
import com.lelloman.simplerss.ui.walkthrough.viewmodel.WalkthroughViewModelImpl
import dagger.Module
import dagger.Provides
import javax.inject.Provider
import javax.inject.Singleton

@Module
open class ViewModelModule {

    @Singleton
    @Provides
    fun provideMap(): Map<Class<out ViewModel>, Provider<out ViewModel>> = mutableMapOf()

    @Provides
    open fun provideArticlesListViewModel(
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

    @Provides
    open fun provideSourcesListViewModel(
        sourcesRepository: SourcesRepository,
        articlesRepository: ArticlesRepository,
        dependencies: BaseViewModel.Dependencies
    ): SourcesListViewModel = SourcesListViewModelImpl(
        sourcesRepository = sourcesRepository,
        articlesRepository = articlesRepository,
        dependencies = dependencies
    )

    @Provides
    open fun provideAddSourceViewModel(
        sourcesRepository: SourcesRepository,
        dependencies: BaseViewModel.Dependencies,
        feedFetcher: FeedFetcher,
        urlValidator: UrlValidator
    ): AddSourceViewModel = AddSourceViewModelImpl(
        dependencies = dependencies,
        sourcesRepository = sourcesRepository,
        feedFetcher = feedFetcher,
        urlValidator = urlValidator
    )

    @Provides
    open fun provideSourceViewModel(
        dependencies: BaseViewModel.Dependencies,
        sourcesRepository: SourcesRepository
    ): SourceViewModel = SourceViewModelImpl(
        dependencies = dependencies,
        sourcesRepository = sourcesRepository
    )

    @Provides
    open fun provideArticleViewModel(
        dependencies: BaseViewModel.Dependencies
    ): ArticleViewModel = ArticleViewModelImpl(
        dependencies = dependencies
    )

    @Provides
    open fun provideSettingsViewModel(
        dependencies: BaseViewModel.Dependencies,
        appSettings: AppSettings,
        semanticTimeProvider: SemanticTimeProvider,
        appDatabase: AppDatabase,
        fileProvider: FileProvider
    ): SettingsViewModel = SettingsViewModelImpl(
        appSettings = appSettings,
        dependencies = dependencies,
        semanticTimeProvider = semanticTimeProvider,
        appDatabase = appDatabase,
        fileProvider = fileProvider
    )

    @Provides
    open fun provideWalkthroughViewModel(
        discoverRepository: DiscoverRepository,
        dependencies: BaseViewModel.Dependencies,
        appSettings: AppSettings,
        urlValidator: UrlValidator,
        htmlSpanner: HtmlSpanner
    ): WalkthroughViewModel = WalkthroughViewModelImpl(
        htmlSpanner = htmlSpanner,
        dependencies = dependencies,
        appSettings = appSettings,
        discoveryRepository = discoverRepository,
        urlValidator = urlValidator
    )

    @Provides
    open fun provideLauncherViewModel(
        dependencies: BaseViewModel.Dependencies,
        appSettings: AppSettings
    ): LauncherViewModel = LauncherViewModelImpl(
        dependencies = dependencies,
        appSettings = appSettings
    )

    @Provides
    open fun provideFoundFeedListViewModel(
        dependencies: BaseViewModel.Dependencies,
        discoverRepository: DiscoverRepository,
        sourcesDao: SourcesDao
    ): FoundFeedListViewModel = FoundFeedListViewModelImpl(
        discoverRepository = discoverRepository,
        dependencies = dependencies,
        sourcesDao = sourcesDao
    )

    @Provides
    open fun provideDiscoverUrlViewModel(
        discoverRepository: DiscoverRepository,
        urlValidator: UrlValidator,
        dependencies: BaseViewModel.Dependencies
    ): DiscoverUrlViewModel = DiscoverUrlViewModelImpl(
        discoverRepository = discoverRepository,
        urlValidator = urlValidator,
        dependencies = dependencies
    )

    @Provides
    open fun provideDebugViewModel(
        appSettings: AppSettings,
        appDatabase: AppDatabase,
        dependencies: BaseViewModel.Dependencies
    ) = DebugViewModel(
        dependencies = dependencies,
        appSettings = appSettings,
        appDatabase = appDatabase
    )
}