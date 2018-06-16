package com.lelloman.read.di

import com.lelloman.read.core.di.ViewModelModule
import com.lelloman.read.ui.articleslist.repository.ArticlesRepository
import com.lelloman.read.ui.articleslist.viewmodel.ArticlesListViewModel
import io.reactivex.Scheduler
import org.mockito.Mockito.mock

class MockViewModelModule : ViewModelModule() {

    val articlesListViewModel: ArticlesListViewModel = mock(ArticlesListViewModel::class.java)

    override fun provideArticlesListViewModel(
        ioScheduler: Scheduler,
        uiScheduler: Scheduler,
        articlesRepository: ArticlesRepository
    ): ArticlesListViewModel = articlesListViewModel
}