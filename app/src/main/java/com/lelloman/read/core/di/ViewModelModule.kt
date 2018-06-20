package com.lelloman.read.core.di

import com.lelloman.read.core.ResourceProvider
import com.lelloman.read.core.TimeDiffCalculator
import com.lelloman.read.core.di.qualifiers.IoScheduler
import com.lelloman.read.core.di.qualifiers.UiScheduler
import com.lelloman.read.core.logger.LoggerFactory
import com.lelloman.read.feed.FeedFetcher
import com.lelloman.read.ui.articleslist.repository.ArticlesRepository
import com.lelloman.read.ui.articleslist.viewmodel.ArticlesListViewModel
import com.lelloman.read.ui.articleslist.viewmodel.ArticlesListViewModelImpl
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
        resourceProvider: ResourceProvider
    ): ArticlesListViewModel = ArticlesListViewModelImpl(
        ioScheduler = ioScheduler,
        uiScheduler = uiScheduler,
        articlesRepository = articlesRepository,
        resourceProvider = resourceProvider
    )

    @Provides
    open fun provideSourcesListViewModel(
        @IoScheduler ioScheduler: Scheduler,
        @UiScheduler uiScheduler: Scheduler,
        sourcesRepository: SourcesRepository,
        resourceProvider: ResourceProvider
    ): SourcesListViewModel = SourcesListViewModelImpl(
        ioScheduler = ioScheduler,
        uiScheduler = uiScheduler,
        sourcesRepository = sourcesRepository,
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
        timeDiffCalculator: TimeDiffCalculator,
        resourceProvider: ResourceProvider,
        sourcesRepository: SourcesRepository
    ): SourceViewModel = SourceViewModelImpl(
        ioScheduler = ioScheduler,
        uiScheduler = uiScheduler,
        timeDiffCalculator = timeDiffCalculator,
        resourceProvider = resourceProvider,
        sourcesRepository = sourcesRepository
    )
}