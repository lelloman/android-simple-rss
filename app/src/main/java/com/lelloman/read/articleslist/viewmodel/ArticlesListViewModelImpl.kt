package com.lelloman.read.articleslist.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.lelloman.read.articleslist.repository.ArticlesRepository
import com.lelloman.read.core.di.qualifiers.IoScheduler
import com.lelloman.read.core.di.qualifiers.UiScheduler
import com.lelloman.read.persistence.model.Article
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable

class ArticlesListViewModelImpl(
    @IoScheduler private val ioScheduler: Scheduler,
    @UiScheduler private val uiScheduler: Scheduler,
    private val articlesRepository: ArticlesRepository
) : ArticlesListViewModel() {

    private val subscriptions = CompositeDisposable()

    override val isLoading by lazy {
        MutableLiveData<Boolean>().also { subscribeIsLoading() }
    }

    override val articles by lazy {
        MutableLiveData<List<Article>>().also { subscribeArticles() }
    }

    override fun refresh() {
        articlesRepository.refresh()
    }

    private fun subscribeArticles() {
        subscriptions.add(
            articlesRepository.fetchArticles()
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe { articles.value = it }
        )
    }

    private fun subscribeIsLoading() {
        subscriptions.add(
            articlesRepository.loading
                .subscribeOn(ioScheduler)
                .subscribe { isLoading.postValue(it) }
        )
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }
}
