package com.lelloman.simplerss.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lelloman.common.di.qualifiers.ViewModelKey
import com.lelloman.common.viewmodel.ViewModelFactory
import com.lelloman.simplerss.ui.articles.viewmodel.ArticleViewModel
import com.lelloman.simplerss.ui.articles.viewmodel.ArticlesListViewModel
import com.lelloman.simplerss.ui.discover.viewmodel.DiscoverUrlViewModel
import com.lelloman.simplerss.ui.discover.viewmodel.FoundFeedListViewModel
import com.lelloman.simplerss.ui.launcher.viewmodel.LauncherViewModel
import com.lelloman.simplerss.ui.settings.viewmodel.SettingsViewModel
import com.lelloman.simplerss.ui.sources.viewmodel.AddSourceViewModel
import com.lelloman.simplerss.ui.sources.viewmodel.SourceViewModel
import com.lelloman.simplerss.ui.sources.viewmodel.SourcesListViewModel
import com.lelloman.simplerss.ui.walkthrough.viewmodel.WalkthroughViewModel
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