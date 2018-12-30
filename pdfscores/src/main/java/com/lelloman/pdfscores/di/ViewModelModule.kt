package com.lelloman.pdfscores.di

import android.arch.lifecycle.ViewModel
import com.lelloman.common.di.qualifiers.IoScheduler
import com.lelloman.common.logger.LoggerFactory
import com.lelloman.common.view.FileProvider
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.pdfscores.pdfrenderer.PdfDocumentFactory
import com.lelloman.pdfscores.persistence.PdfScoresRepository
import com.lelloman.pdfscores.ui.pdfviewer.PdfViewerViewModel
import com.lelloman.pdfscores.ui.pdfviewer.PdfViewerViewModelImpl
import com.lelloman.pdfscores.ui.pdfscoreslist.viewmodel.PdfScoresListViewModel
import com.lelloman.pdfscores.ui.pdfscoreslist.viewmodel.PdfScoresListViewModelImpl
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
    open fun provideRecentScoresViewModel(
        dependencies: BaseViewModel.Dependencies,
        @IoScheduler ioScheduler: Scheduler,
        pdfScoresRepository: PdfScoresRepository
    ): PdfScoresListViewModel = PdfScoresListViewModelImpl(
        dependencies = dependencies,
        ioScheduler = ioScheduler,
        pdfScoresRepository = pdfScoresRepository
    )

    @Provides
    open fun providePdfViewerViewModel(
        dependencies: BaseViewModel.Dependencies,
        loggerFactory: LoggerFactory,
        fileProvider: FileProvider,
        pdfDocumentFactory: PdfDocumentFactory
    ): PdfViewerViewModel = PdfViewerViewModelImpl(
        dependencies = dependencies,
        loggerFactory = loggerFactory,
        pdfDocumentFactory = pdfDocumentFactory,
        fileProvider = fileProvider
    )
}