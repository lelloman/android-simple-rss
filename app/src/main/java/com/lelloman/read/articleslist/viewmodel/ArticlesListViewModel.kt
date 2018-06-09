package com.lelloman.read.articleslist.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.lelloman.read.articleslist.blu.ArticlesRepository
import com.lelloman.read.articleslist.model.Article
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ArticlesListViewModel : ViewModel() {

    private val subscriptions = CompositeDisposable()
    private val ioScheduler = Schedulers.io()
    private val uiScheduler = AndroidSchedulers.mainThread()
    private val articlesRepository = ArticlesRepository()

    val isLoading = MutableLiveData<Boolean>()

    val articles by lazy {
        MutableLiveData<List<Article>>().also {
            loadArticles()
        }
    }

    private fun loadArticles() {
        subscriptions.add(
            articlesRepository.fetchArticles()
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe {
                    articles.value = it
                }
        )

        subscriptions.add(
            articlesRepository.loading
                .observeOn(uiScheduler)
                .subscribe { isLoading.value = it }
        )
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }
}