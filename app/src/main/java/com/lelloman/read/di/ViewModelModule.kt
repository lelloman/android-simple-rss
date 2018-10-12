package com.lelloman.read.di

import android.arch.lifecycle.ViewModel
import com.lelloman.common.di.qualifiers.IoScheduler
import com.lelloman.common.di.qualifiers.UiScheduler
import com.lelloman.common.logger.LoggerFactory
import com.lelloman.common.utils.UrlValidator
import com.lelloman.common.view.SemanticTimeProvider
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.read.feed.fetcher.FeedFetcher
import com.lelloman.read.persistence.settings.AppSettings
import com.lelloman.read.ui.articles.viewmodel.ArticleViewModel
import com.lelloman.read.ui.articles.viewmodel.ArticleViewModelImpl
import com.lelloman.read.ui.articles.viewmodel.ArticlesListViewModel
import com.lelloman.read.ui.articles.viewmodel.ArticlesListViewModelImpl
import com.lelloman.read.ui.common.repository.ArticlesRepository
import com.lelloman.read.ui.common.repository.DiscoverRepository
import com.lelloman.read.ui.common.repository.SourcesRepository
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
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import javax.inject.Provider
import javax.inject.Singleton

@Module
open class ViewModelModule {

    @Singleton
    @Provides
    fun provideMap(): Map<Class<out ViewModel>, Provider<out ViewModel>> = mutableMapOf()

    @Provides
    open fun provideArticlesListViewModel(
        @IoScheduler ioScheduler: Scheduler,
        @UiScheduler uiScheduler: Scheduler,
        articlesRepository: ArticlesRepository,
        sourcesRepository: SourcesRepository,
        discoverRepository: DiscoverRepository,
        dependencies: BaseViewModel.Dependencies,
        appSettings: AppSettings
    ): ArticlesListViewModel = ArticlesListViewModelImpl(
        ioScheduler = ioScheduler,
        uiScheduler = uiScheduler,
        articlesRepository = articlesRepository,
        sourcesRepository = sourcesRepository,
        discoverRepository = discoverRepository,
        appSettings = appSettings,
        dependencies = dependencies
    )

    @Provides
    open fun provideSourcesListViewModel(
        @IoScheduler ioScheduler: Scheduler,
        @UiScheduler uiScheduler: Scheduler,
        sourcesRepository: SourcesRepository,
        articlesRepository: ArticlesRepository,
        dependencies: BaseViewModel.Dependencies
    ): SourcesListViewModel = SourcesListViewModelImpl(
        ioScheduler = ioScheduler,
        uiScheduler = uiScheduler,
        sourcesRepository = sourcesRepository,
        articlesRepository = articlesRepository,
        dependencies = dependencies
    )

    @Provides
    open fun provideAddSourceViewModel(
        @IoScheduler ioScheduler: Scheduler,
        @UiScheduler uiScheduler: Scheduler,
        sourcesRepository: SourcesRepository,
        dependencies: BaseViewModel.Dependencies,
        feedFetcher: FeedFetcher,
        loggerFactory: LoggerFactory,
        urlValidator: UrlValidator
    ): AddSourceViewModel = AddSourceViewModelImpl(
        dependencies = dependencies,
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
        dependencies: BaseViewModel.Dependencies,
        sourcesRepository: SourcesRepository
    ): SourceViewModel = SourceViewModelImpl(
        ioScheduler = ioScheduler,
        uiScheduler = uiScheduler,
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
        @IoScheduler ioScheduler: Scheduler,
        @UiScheduler uiScheduler: Scheduler,
        dependencies: BaseViewModel.Dependencies,
        appSettings: AppSettings,
        semanticTimeProvider: SemanticTimeProvider
    ): SettingsViewModel = SettingsViewModelImpl(
        ioScheduler = ioScheduler,
        uiScheduler = uiScheduler,
        appSettings = appSettings,
        dependencies = dependencies,
        semanticTimeProvider = semanticTimeProvider
    )

    @Provides
    open fun provideWalkthroughViewModel(
        @UiScheduler uiScheduler: Scheduler,
        @IoScheduler ioScheduler: Scheduler,
        discoverRepository: DiscoverRepository,
        dependencies: BaseViewModel.Dependencies,
        appSettings: AppSettings,
        urlValidator: UrlValidator
    ): WalkthroughViewModel = WalkthroughViewModelImpl(
        dependencies = dependencies,
        appSettings = appSettings,
        ioScheduler = ioScheduler,
        uiScheduler = uiScheduler,
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
        @IoScheduler ioScheduler: Scheduler,
        @UiScheduler uiScheduler: Scheduler,
        discoverRepository: DiscoverRepository
    ): FoundFeedListViewModel = FoundFeedListViewModelImpl(
        ioScheduler = ioScheduler,
        uiScheduler = uiScheduler,
        discoverRepository = discoverRepository,
        dependencies = dependencies
    )

    @Provides
    open fun provideDiscoverUrlViewModel(
        @IoScheduler ioScheduler: Scheduler,
        @UiScheduler uiScheduler: Scheduler,
        discoverRepository: DiscoverRepository,
        urlValidator: UrlValidator,
        dependencies: BaseViewModel.Dependencies
    ): DiscoverUrlViewModel = DiscoverUrlViewModelImpl(
        ioScheduler = ioScheduler,
        uiScheduler = uiScheduler,
        discoverRepository = discoverRepository,
        urlValidator = urlValidator,
        dependencies = dependencies
    )
}