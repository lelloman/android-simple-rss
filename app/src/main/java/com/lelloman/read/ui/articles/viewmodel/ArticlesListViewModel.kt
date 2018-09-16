package com.lelloman.read.ui.articles.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.lelloman.common.view.ResourceProvider
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.read.persistence.db.model.SourceArticle

abstract class ArticlesListViewModel(
    resourceProvider: ResourceProvider
) : BaseViewModel(resourceProvider) {

    abstract val isLoading: MutableLiveData<Boolean>

    abstract val emptyViewVisible: MutableLiveData<Boolean>
    abstract val emptyViewDescriptionText: MutableLiveData<String>
    abstract val emptyViewButtonText: MutableLiveData<String>

    abstract val articles: MutableLiveData<List<SourceArticle>>

    abstract fun refresh()

    abstract fun onSourcesClicked()

    abstract fun onEmptyViewButtonClicked()

    abstract fun onArticleClicked(article: SourceArticle)

    abstract fun onSettingsClicked()

    abstract fun onDiscoverSourceClicked()
}
