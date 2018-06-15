package com.lelloman.read.articleslist.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.lelloman.read.articleslist.repository.ArticlesRepository
import com.lelloman.read.core.ResourceProvider
import com.lelloman.read.core.navigation.NavigationScreen
import com.lelloman.read.core.navigation.ScreenNavigationEvent
import com.lelloman.read.persistence.model.Article
import com.lelloman.read.utils.LazyLiveData
import io.reactivex.Scheduler

class ArticlesListViewModelImpl(
    private val ioScheduler: Scheduler,
    private val uiScheduler: Scheduler,
    private val articlesRepository: ArticlesRepository,
    resourceProvider: ResourceProvider
) : ArticlesListViewModel(resourceProvider) {

    override val isLoading: MutableLiveData<Boolean> by LazyLiveData({
        subscription {
            articlesRepository.loading
                .subscribeOn(ioScheduler)
                .subscribe { isLoading.postValue(it) }
        }
    })

    override val articles: MutableLiveData<List<Article>> by LazyLiveData({
        subscription {
            articlesRepository.fetchArticles()
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe { articles.value = it }
        }
    })

    override fun refresh() = articlesRepository.refresh()

    override fun onSourcesClicked() = navigate(ScreenNavigationEvent(NavigationScreen.SOURCES_LIST))
}
