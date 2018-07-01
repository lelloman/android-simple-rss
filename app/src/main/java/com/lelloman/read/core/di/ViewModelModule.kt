package com.lelloman.read.core.di

import com.lelloman.read.core.ResourceProvider
import com.lelloman.read.core.SemanticTimeProvider
import com.lelloman.read.core.di.qualifiers.IoScheduler
import com.lelloman.read.core.di.qualifiers.UiScheduler
import com.lelloman.read.core.logger.LoggerFactory
import com.lelloman.read.feed.FeedFetcher
import com.lelloman.read.persistence.settings.AppSettings
import com.lelloman.read.ui.articles.repository.ArticlesRepository
import com.lelloman.read.ui.articles.viewmodel.ArticleViewModel
import com.lelloman.read.ui.articles.viewmodel.ArticleViewModelImpl
import com.lelloman.read.ui.articles.viewmodel.ArticlesListViewModel
import com.lelloman.read.ui.articles.viewmodel.ArticlesListViewModelImpl
import com.lelloman.read.ui.settings.viewmodel.SettingsViewModel
import com.lelloman.read.ui.settings.viewmodel.SettingsViewModelImpl
import com.lelloman.read.ui.sources.repository.SourcesRepository
import com.lelloman.read.ui.sources.viewmodel.AddSourceViewModel
import com.lelloman.read.ui.sources.viewmodel.AddSourceViewModelImpl
import com.lelloman.read.ui.sources.viewmodel.SourceViewModel
import com.lelloman.read.ui.sources.viewmodel.SourceViewModelImpl
import com.lelloman.read.ui.sources.viewmodel.SourcesListViewModel
import com.lelloman.read.ui.sources.viewmodel.SourcesListViewModelImpl
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
        appSettings: AppSettings
    ): ArticlesListViewModel = ArticlesListViewModelImpl(
        ioScheduler = ioScheduler,
        uiScheduler = uiScheduler,
        articlesRepository = articlesRepository,
        resourceProvider = resourceProvider,
        sourcesRepository = sourcesRepository,
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
}