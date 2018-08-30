package com.lelloman.read.core.di

import com.lelloman.read.core.ActionTokenProvider
import com.lelloman.read.core.ResourceProvider
import com.lelloman.read.core.SemanticTimeProvider
import com.lelloman.read.core.di.qualifiers.IoScheduler
import com.lelloman.read.core.di.qualifiers.UiScheduler
import com.lelloman.read.core.logger.LoggerFactory
import com.lelloman.read.feed.fetcher.FeedFetcher
import com.lelloman.read.persistence.settings.AppSettings
import com.lelloman.read.ui.articles.viewmodel.ArticleViewModel
import com.lelloman.read.ui.articles.viewmodel.ArticleViewModelImpl
import com.lelloman.read.ui.articles.viewmodel.ArticlesListViewModel
import com.lelloman.read.ui.articles.viewmodel.ArticlesListViewModelImpl
import com.lelloman.read.ui.common.repository.ArticlesRepository
import com.lelloman.read.ui.common.repository.SourcesRepository
import com.lelloman.read.ui.common.repository.DiscoverRepository
import com.lelloman.read.ui.discover.viewmodel.DiscoverUrlViewModel
import com.lelloman.read.ui.discover.viewmodel.DiscoverUrlViewModelImpl
import com.lelloman.read.ui.discover.viewmodel.FoundFeedListViewModel
import com.lelloman.read.ui.discover.viewmodel.FoundFeedListViewModelImpl
import com.lelloman.read.ui.launcher.viewmodel.LauncherViewModel
import com.lelloman.read.ui.launcher.viewmodel.LauncherViewModelImpl
import com.lelloman.read.ui.settings.viewmodel.SettingsViewModel
import com.lelloman.read.ui.settings.viewmodel.SettingsViewModelImpl
import com.lelloman.read.ui.sources.viewmodel.AddSourceViewModel
import com.lelloman.read.ui.sources.viewmodel.AddSourceViewModelImpl
import com.lelloman.read.ui.sources.viewmodel.SourceViewModel
import com.lelloman.read.ui.sources.viewmodel.SourceViewModelImpl
import com.lelloman.read.ui.sources.viewmodel.SourcesListViewModel
import com.lelloman.read.ui.sources.viewmodel.SourcesListViewModelImpl
import com.lelloman.read.ui.walkthrough.viewmodel.WalkthroughViewModel
import com.lelloman.read.ui.walkthrough.viewmodel.WalkthroughViewModelImpl
import com.lelloman.read.utils.UrlValidator
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
        resourceProvider: ResourceProvider,
        sourcesRepository: SourcesRepository,
        discoverRepository: DiscoverRepository,
        appSettings: AppSettings
    ): ArticlesListViewModel = ArticlesListViewModelImpl(
        ioScheduler = ioScheduler,
        uiScheduler = uiScheduler,
        articlesRepository = articlesRepository,
        resourceProvider = resourceProvider,
        sourcesRepository = sourcesRepository,
        discoverRepository = discoverRepository,
        appSettings = appSettings
    )

    @Provides
    open fun provideSourcesListViewModel(
        @IoScheduler ioScheduler: Scheduler,
        @UiScheduler uiScheduler: Scheduler,
        sourcesRepository: SourcesRepository,
        articlesRepository: ArticlesRepository,
        resourceProvider: ResourceProvider
    ): SourcesListViewModel = SourcesListViewModelImpl(
        ioScheduler = ioScheduler,
        uiScheduler = uiScheduler,
        sourcesRepository = sourcesRepository,
        articlesRepository = articlesRepository,
        resourceProvider = resourceProvider
    )

    @Provides
    open fun provideAddSourceViewModel(
        @IoScheduler ioScheduler: Scheduler,
        @UiScheduler uiScheduler: Scheduler,
        sourcesRepository: SourcesRepository,
        resourceProvider: ResourceProvider,
        feedFetcher: FeedFetcher,
        loggerFactory: LoggerFactory,
        urlValidator: UrlValidator
    ): AddSourceViewModel = AddSourceViewModelImpl(
        resourceProvider = resourceProvider,
        ioScheduler = ioScheduler,
        uiScheduler = uiScheduler,
        sourcesRepository = sourcesRepository,
        feedFetcher = feedFetcher,
        loggerFactory = loggerFactory,
        urlValidator = urlValidator
    )

    @Provides
    open fun provideSourceViewModel(
        @IoScheduler ioScheduler: Scheduler,
        @UiScheduler uiScheduler: Scheduler,
        semanticTimeProvider: SemanticTimeProvider,
        resourceProvider: ResourceProvider,
        sourcesRepository: SourcesRepository
    ): SourceViewModel = SourceViewModelImpl(
        ioScheduler = ioScheduler,
        uiScheduler = uiScheduler,
        semanticTimeProvider = semanticTimeProvider,
        resourceProvider = resourceProvider,
        sourcesRepository = sourcesRepository
    )

    @Provides
    open fun provideArticleViewModel(
        resourceProvider: ResourceProvider
    ): ArticleViewModel = ArticleViewModelImpl(
        resourceProvider = resourceProvider
    )

    @Provides
    open fun provideSettingsViewModel(
        @IoScheduler ioScheduler: Scheduler,
        @UiScheduler uiScheduler: Scheduler,
        resourceProvider: ResourceProvider,
        appSettings: AppSettings,
        semanticTimeProvider: SemanticTimeProvider
    ): SettingsViewModel = SettingsViewModelImpl(
        ioScheduler = ioScheduler,
        uiScheduler = uiScheduler,
        appSettings = appSettings,
        resourceProvider = resourceProvider,
        semanticTimeProvider = semanticTimeProvider
    )

    @Provides
    open fun provideWalkthroughViewModel(
        @UiScheduler uiScheduler: Scheduler,
        @IoScheduler ioScheduler: Scheduler,
        discoverRepository: DiscoverRepository,
        resourceProvider: ResourceProvider,
        actionTokenProvider: ActionTokenProvider,
        appSettings: AppSettings,
        urlValidator: UrlValidator
    ): WalkthroughViewModel = WalkthroughViewModelImpl(
        resourceProvider = resourceProvider,
        actionTokenProvider = actionTokenProvider,
        appSettings = appSettings,
        ioScheduler = ioScheduler,
        uiScheduler = uiScheduler,
        discoveryRepository = discoverRepository,
        urlValidator = urlValidator
    )

    @Provides
    open fun provideLauncherViewModel(
        resourceProvider: ResourceProvider,
        actionTokenProvider: ActionTokenProvider,
        appSettings: AppSettings
    ): LauncherViewModel = LauncherViewModelImpl(
        resourceProvider = resourceProvider,
        actionTokenProvider = actionTokenProvider,
        appSettings = appSettings
    )

    @Provides
    open fun provideFoundFeedListViewModel(
        resourceProvider: ResourceProvider,
        actionTokenProvider: ActionTokenProvider,
        @IoScheduler ioScheduler: Scheduler,
        @UiScheduler uiScheduler: Scheduler,
        discoverRepository: DiscoverRepository
    ): FoundFeedListViewModel = FoundFeedListViewModelImpl(
        ioScheduler = ioScheduler,
        uiScheduler = uiScheduler,
        discoverRepository = discoverRepository,
        resourceProvider = resourceProvider,
        actionTokenProvider = actionTokenProvider
    )

    @Provides
    open fun provideDiscoverUrlViewModel(
        @IoScheduler ioScheduler: Scheduler,
        @UiScheduler uiScheduler: Scheduler,
        discoverRepository: DiscoverRepository,
        urlValidator: UrlValidator,
        resourceProvider: ResourceProvider,
        actionTokenProvider: ActionTokenProvider
    ): DiscoverUrlViewModel = DiscoverUrlViewModelImpl(
        ioScheduler = ioScheduler,
        uiScheduler = uiScheduler,
        discoverRepository = discoverRepository,
        urlValidator = urlValidator,
        resourceProvider = resourceProvider,
        actionTokenProvider = actionTokenProvider
    )
}