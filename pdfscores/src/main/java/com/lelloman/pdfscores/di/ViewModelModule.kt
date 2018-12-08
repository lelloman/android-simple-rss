package com.lelloman.pdfscores.di

import android.arch.lifecycle.ViewModel
import com.lelloman.common.di.qualifiers.IoScheduler
import com.lelloman.common.logger.LoggerFactory
import com.lelloman.common.view.FileProvider
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.pdfscores.pdfrenderer.PdfDocumentFactory
import com.lelloman.pdfscores.persistence.PdfScoresDao
import com.lelloman.pdfscores.ui.pdfviewer.PdfViewerViewModel
import com.lelloman.pdfscores.ui.pdfviewer.PdfViewerViewModelImpl
import com.lelloman.pdfscores.ui.recentscores.viewmodel.RecentScoresViewModel
import com.lelloman.pdfscores.ui.recentscores.viewmodel.RecentScoresViewModelImpl
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import javax.inject.Provider
import javax.inject.Singleton

@Module
class ViewModelModule {

    @Singleton
    @Provides
    fun provideMap(): Map<Class<out ViewModel>, Provider<out ViewModel>> = mutableMapOf()

    @Provides
    open fun provideRecentScoresViewModel(
        dependencies: BaseViewModel.Dependencies,
        @IoScheduler ioScheduler: Scheduler,
        pdfScoresDao: PdfScoresDao
    ): RecentScoresViewModel = RecentScoresViewModelImpl(
        dependencies = dependencies,
        ioScheduler = ioScheduler,
        pdfScoresDao = pdfScoresDao
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