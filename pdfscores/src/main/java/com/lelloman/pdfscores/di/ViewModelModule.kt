package com.lelloman.pdfscores.di

import android.arch.lifecycle.ViewModel
import com.lelloman.common.di.qualifiers.IoScheduler
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.pdfscores.persistence.PdfScoresDao
import com.lelloman.pdfscores.recentscores.viewmodel.RecentScoresViewModel
import com.lelloman.pdfscores.recentscores.viewmodel.RecentScoresViewModelImpl
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
}