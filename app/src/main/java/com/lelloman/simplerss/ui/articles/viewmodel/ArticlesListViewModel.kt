package com.lelloman.simplerss.ui.articles.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.lelloman.common.viewmodel.BaseViewModel

abstract class ArticlesListViewModel(dependencies: Dependencies) : BaseViewModel(dependencies) {

    abstract val isLoading: MutableLiveData<Boolean>

    abstract val emptyViewVisible: MutableLiveData<Boolean>
    abstract val emptyViewDescriptionText: MutableLiveData<String>
    abstract val emptyViewButtonText: MutableLiveData<String>

    abstract val articles: MutableLiveData<List<com.lelloman.simplerss.persistence.db.model.SourceArticle>>

    abstract fun refresh()

    abstract fun onSourcesClicked()

    abstract fun onEmptyViewButtonClicked()

    abstract fun onArticleClicked(article: com.lelloman.simplerss.persistence.db.model.SourceArticle)

    abstract fun onSettingsClicked()

    abstract fun onDiscoverSourceClicked()
}
