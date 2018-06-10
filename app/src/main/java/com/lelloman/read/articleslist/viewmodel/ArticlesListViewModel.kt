package com.lelloman.read.articleslist.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.lelloman.read.articleslist.model.Article
import com.lelloman.read.articleslist.repository.ArticlesRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ArticlesListViewModel : ViewModel() {

    private val subscriptions = CompositeDisposable()
    private val ioScheduler = Schedulers.io()
    private val uiScheduler = AndroidSchedulers.mainThread()
    private val articlesRepository = ArticlesRepository(ioScheduler)

    val isLoading by lazy {
        MutableLiveData<Boolean>().also { subscribeIsLoading() }
    }

    val articles by lazy {
        MutableLiveData<List<Article>>().also { subscribeArticles() }
    }

    fun refresh() {
        articlesRepository.refresh()
    }

    private fun subscribeArticles() {
        subscriptions.add(
            articlesRepository.fetchArticles()
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe {
                    articles.value = it
                }
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
