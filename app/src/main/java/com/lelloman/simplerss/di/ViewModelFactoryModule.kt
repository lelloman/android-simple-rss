package com.lelloman.simplerss.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.lelloman.common.di.qualifiers.ViewModelKey
import com.lelloman.common.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelFactoryModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(com.lelloman.simplerss.ui.articles.viewmodel.ArticlesListViewModel::class)
    abstract fun bindArticlesListViewModel(articlesListViewModel: com.lelloman.simplerss.ui.articles.viewmodel.ArticlesListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(com.lelloman.simplerss.ui.sources.viewmodel.SourcesListViewModel::class)
    abstract fun bindSourcesListViewModel(sourcesListViewModel: com.lelloman.simplerss.ui.sources.viewmodel.SourcesListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(com.lelloman.simplerss.ui.sources.viewmodel.AddSourceViewModel::class)
    abstract fun bindAddSourceViewModel(addSourceViewModel: com.lelloman.simplerss.ui.sources.viewmodel.AddSourceViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(com.lelloman.simplerss.ui.sources.viewmodel.SourceViewModel::class)
    abstract fun bindSourceViewModel(sourceViewModel: com.lelloman.simplerss.ui.sources.viewmodel.SourceViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(com.lelloman.simplerss.ui.articles.viewmodel.ArticleViewModel::class)
    abstract fun bindArticleViewModel(articleViewModel: com.lelloman.simplerss.ui.articles.viewmodel.ArticleViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(com.lelloman.simplerss.ui.settings.viewmodel.SettingsViewModel::class)
    abstract fun bindSettingsViewModel(settingsViewModel: com.lelloman.simplerss.ui.settings.viewmodel.SettingsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(com.lelloman.simplerss.ui.walkthrough.viewmodel.WalkthroughViewModel::class)
    abstract fun bindWalkthroughViewModel(walkthroughViewModel: com.lelloman.simplerss.ui.walkthrough.viewmodel.WalkthroughViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(com.lelloman.simplerss.ui.launcher.viewmodel.LauncherViewModel::class)
    abstract fun bindLauncherViewModel(launcherViewModel: com.lelloman.simplerss.ui.launcher.viewmodel.LauncherViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(com.lelloman.simplerss.ui.discover.viewmodel.FoundFeedListViewModel::class)
    abstract fun bindFoundFeedListViewModek(foundFeedListViewModel: com.lelloman.simplerss.ui.discover.viewmodel.FoundFeedListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(com.lelloman.simplerss.ui.discover.viewmodel.DiscoverUrlViewModel::class)
    abstract fun bindDiscoverUrlViewModel(discoverUrlViewModel: com.lelloman.simplerss.ui.discover.viewmodel.DiscoverUrlViewModel): ViewModel
}