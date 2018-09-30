package com.lelloman.deviceinfo.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.lelloman.common.di.qualifiers.ViewModelKey
import com.lelloman.common.viewmodel.ViewModelFactory
import com.lelloman.deviceinfo.ui.viewmodel.InfoListViewModel
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
    @ViewModelKey(InfoListViewModel::class)
    abstract fun bindInfoListViewModel(viewModel: InfoListViewModel): ViewModel
}