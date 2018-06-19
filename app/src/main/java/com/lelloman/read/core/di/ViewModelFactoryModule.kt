package com.lelloman.read.core.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.lelloman.read.core.viewmodel.ViewModelFactory
import com.lelloman.read.ui.articleslist.viewmodel.ArticlesListViewModel
import com.lelloman.read.ui.sources.viewmodel.AddSourceViewModel
import com.lelloman.read.ui.sources.viewmodel.SourceViewModel
import com.lelloman.read.ui.sources.viewmodel.SourcesListViewModel
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
}