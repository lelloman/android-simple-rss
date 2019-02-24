package com.lelloman.simplerss.ui.articles.viewmodel

import androidx.lifecycle.MutableLiveData
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.simplerss.persistence.db.model.SourceArticle

abstract class ArticlesListViewModel(dependencies: Dependencies) : BaseViewModel(dependencies) {

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

    abstract fun onDebugClicked()
}
