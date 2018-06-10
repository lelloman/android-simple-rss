package com.lelloman.read.di

import com.lelloman.read.articleslist.repository.ArticlesRepository
import com.lelloman.read.articleslist.viewmodel.ArticlesListViewModel
import com.lelloman.read.core.di.ViewModelModule
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