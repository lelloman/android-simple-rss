package com.lelloman.pdfscores.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.lelloman.common.di.qualifiers.ViewModelKey
import com.lelloman.common.viewmodel.ViewModelFactory
import com.lelloman.pdfscores.ui.pdfviewer.PdfViewerViewModel
import com.lelloman.pdfscores.ui.pdfscoreslist.viewmodel.PdfScoresListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelFactoryModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(PdfScoresListViewModel::class)
    abstract fun bindRecentScoresViewModel(recentScoresViewModel: PdfScoresListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PdfViewerViewModel::class)
    abstract fun bindPdfViewerViewModel(pdfViewerViewModel: PdfViewerViewModel): ViewModel
}