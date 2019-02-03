package com.lelloman.simplerss.di

import android.arch.lifecycle.ViewModel
import com.lelloman.common.logger.LoggerFactory
import com.lelloman.common.utils.UrlValidator
import com.lelloman.common.view.SemanticTimeProvider
import com.lelloman.common.viewmodel.BaseViewModel
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
        articlesRepository: com.lelloman.simplerss.ui.common.repository.ArticlesRepository,
        sourcesRepository: com.lelloman.simplerss.ui.common.repository.SourcesRepository,
        discoverRepository: com.lelloman.simplerss.ui.common.repository.DiscoverRepository,
        dependencies: BaseViewModel.Dependencies,
        appSettings: com.lelloman.simplerss.persistence.settings.AppSettings
    ): com.lelloman.simplerss.ui.articles.viewmodel.ArticlesListViewModel = com.lelloman.simplerss.ui.articles.viewmodel.ArticlesListViewModelImpl(
        articlesRepository = articlesRepository,
        sourcesRepository = sourcesRepository,
        discoverRepository = discoverRepository,
        appSettings = appSettings,
        dependencies = dependencies
    )

    @Provides
    open fun provideSourcesListViewModel(
        sourcesRepository: com.lelloman.simplerss.ui.common.repository.SourcesRepository,
        articlesRepository: com.lelloman.simplerss.ui.common.repository.ArticlesRepository,
        dependencies: BaseViewModel.Dependencies
    ): com.lelloman.simplerss.ui.sources.viewmodel.SourcesListViewModel = com.lelloman.simplerss.ui.sources.viewmodel.SourcesListViewModelImpl(
        sourcesRepository = sourcesRepository,
        articlesRepository = articlesRepository,
        dependencies = dependencies
    )

    @Provides
    open fun provideAddSourceViewModel(
        sourcesRepository: com.lelloman.simplerss.ui.common.repository.SourcesRepository,
        dependencies: BaseViewModel.Dependencies,
        feedFetcher: com.lelloman.simplerss.feed.fetcher.FeedFetcher,
        loggerFactory: LoggerFactory,
        urlValidator: UrlValidator
    ): com.lelloman.simplerss.ui.sources.viewmodel.AddSourceViewModel = com.lelloman.simplerss.ui.sources.viewmodel.AddSourceViewModelImpl(
        dependencies = dependencies,
        sourcesRepository = sourcesRepository,
        feedFetcher = feedFetcher,
        loggerFactory = loggerFactory,
        urlValidator = urlValidator
    )

    @Provides
    open fun provideSourceViewModel(
        dependencies: BaseViewModel.Dependencies,
        sourcesRepository: com.lelloman.simplerss.ui.common.repository.SourcesRepository
    ): com.lelloman.simplerss.ui.sources.viewmodel.SourceViewModel = com.lelloman.simplerss.ui.sources.viewmodel.SourceViewModelImpl(
        dependencies = dependencies,
        sourcesRepository = sourcesRepository
    )

    @Provides
    open fun provideArticleViewModel(
        dependencies: BaseViewModel.Dependencies
    ): com.lelloman.simplerss.ui.articles.viewmodel.ArticleViewModel = com.lelloman.simplerss.ui.articles.viewmodel.ArticleViewModelImpl(
        dependencies = dependencies
    )

    @Provides
    open fun provideSettingsViewModel(
        dependencies: BaseViewModel.Dependencies,
        appSettings: com.lelloman.simplerss.persistence.settings.AppSettings,
        semanticTimeProvider: SemanticTimeProvider
    ): com.lelloman.simplerss.ui.settings.viewmodel.SettingsViewModel = com.lelloman.simplerss.ui.settings.viewmodel.SettingsViewModelImpl(
        appSettings = appSettings,
        dependencies = dependencies,
        semanticTimeProvider = semanticTimeProvider
    )

    @Provides
    open fun provideWalkthroughViewModel(
        discoverRepository: com.lelloman.simplerss.ui.common.repository.DiscoverRepository,
        dependencies: BaseViewModel.Dependencies,
        appSettings: com.lelloman.simplerss.persistence.settings.AppSettings,
        urlValidator: UrlValidator
    ): com.lelloman.simplerss.ui.walkthrough.viewmodel.WalkthroughViewModel = com.lelloman.simplerss.ui.walkthrough.viewmodel.WalkthroughViewModelImpl(
        dependencies = dependencies,
        appSettings = appSettings,
        discoveryRepository = discoverRepository,
        urlValidator = urlValidator
    )

    @Provides
    open fun provideLauncherViewModel(
        dependencies: BaseViewModel.Dependencies,
        appSettings: com.lelloman.simplerss.persistence.settings.AppSettings
    ): com.lelloman.simplerss.ui.launcher.viewmodel.LauncherViewModel = com.lelloman.simplerss.ui.launcher.viewmodel.LauncherViewModelImpl(
        dependencies = dependencies,
        appSettings = appSettings
    )

    @Provides
    open fun provideFoundFeedListViewModel(
        dependencies: BaseViewModel.Dependencies,
        discoverRepository: com.lelloman.simplerss.ui.common.repository.DiscoverRepository,
        sourcesDao: com.lelloman.simplerss.persistence.db.SourcesDao
    ): com.lelloman.simplerss.ui.discover.viewmodel.FoundFeedListViewModel = com.lelloman.simplerss.ui.discover.viewmodel.FoundFeedListViewModelImpl(
        discoverRepository = discoverRepository,
        dependencies = dependencies,
        sourcesDao = sourcesDao
    )

    @Provides
    open fun provideDiscoverUrlViewModel(
        discoverRepository: com.lelloman.simplerss.ui.common.repository.DiscoverRepository,
        urlValidator: UrlValidator,
        dependencies: BaseViewModel.Dependencies
    ): com.lelloman.simplerss.ui.discover.viewmodel.DiscoverUrlViewModel = com.lelloman.simplerss.ui.discover.viewmodel.DiscoverUrlViewModelImpl(
        discoverRepository = discoverRepository,
        urlValidator = urlValidator,
        dependencies = dependencies
    )
}