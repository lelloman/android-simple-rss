package com.lelloman.read.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.lelloman.common.viewmodel.ViewModelFactory
import com.lelloman.read.ui.articles.viewmodel.ArticleViewModel
import com.lelloman.read.ui.articles.viewmodel.ArticlesListViewModel
import com.lelloman.read.ui.discover.viewmodel.DiscoverUrlViewModel
import com.lelloman.read.ui.discover.viewmodel.FoundFeedListViewModel
import com.lelloman.read.ui.launcher.viewmodel.LauncherViewModel
import com.lelloman.read.ui.settings.viewmodel.SettingsViewModel
import com.lelloman.read.ui.sources.viewmodel.AddSourceViewModel
import com.lelloman.read.ui.sources.viewmodel.SourceViewModel
import com.lelloman.read.ui.sources.viewmodel.SourcesListViewModel
import com.lelloman.read.ui.walkthrough.viewmodel.WalkthroughViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelFactoryModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ArticlesListViewModel::class)
    abstract fun bindArticlesListViewModel(articlesListViewModel: ArticlesListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SourcesListViewModel::class)
    abstract fun bindSourcesListViewModel(sourcesListViewModel: SourcesListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddSourceViewModel::class)
    abstract fun bindAddSourceViewModel(addSourceViewModel: AddSourceViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SourceViewModel::class)
    abstract fun bindSourceViewModel(sourceViewModel: SourceViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ArticleViewModel::class)
    abstract fun bindArticleViewModel(articleViewModel: ArticleViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindSettingsViewModel(settingsViewModel: SettingsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WalkthroughViewModel::class)
    abstract fun bindWalkthroughViewModel(walkthroughViewModel: WalkthroughViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LauncherViewModel::class)
    abstract fun bindLauncherViewModel(launcherViewModel: LauncherViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FoundFeedListViewModel::class)
    abstract fun bindFoundFeedListViewModek(foundFeedListViewModel: FoundFeedListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DiscoverUrlViewModel::class)
    abstract fun bindDiscoverUrlViewModel(discoverUrlViewModel: DiscoverUrlViewModel): ViewModel
}