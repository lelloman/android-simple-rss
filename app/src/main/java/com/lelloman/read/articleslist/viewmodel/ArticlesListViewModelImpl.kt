package com.lelloman.read.articleslist.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.lelloman.read.articleslist.repository.ArticlesRepository
import com.lelloman.read.core.di.qualifiers.IoScheduler
import com.lelloman.read.core.di.qualifiers.UiScheduler
import com.lelloman.read.core.navigation.NavigationScreen
import com.lelloman.read.core.navigation.ScreenNavigationEvent
import com.lelloman.read.persistence.model.Article
import io.reactivex.Scheduler

class ArticlesListViewModelImpl(
    @IoScheduler private val ioScheduler: Scheduler,
    @UiScheduler private val uiScheduler: Scheduler,
    private val articlesRepository: ArticlesRepository
) : ArticlesListViewModel() {

    override val isLoading: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().also { subscribeIsLoading() }
    }

    override val articles: MutableLiveData<List<Article>> by lazy {
        MutableLiveData<List<Article>>().also { subscribeArticles() }
    }

    override fun refresh() {
        articlesRepository.refresh()
    }

    private fun subscribeArticles() = subscribe {
        articlesRepository.fetchArticles()
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe { articles.value = it }
    }

    private fun subscribeIsLoading() = subscribe {
        articlesRepository.loading
            .subscribeOn(ioScheduler)
            .subscribe { isLoading.postValue(it) }
    }

    override fun onSourcesClicked() {
        navigation.postValue(ScreenNavigationEvent(NavigationScreen.SOURCES_LIST))
    }

}
