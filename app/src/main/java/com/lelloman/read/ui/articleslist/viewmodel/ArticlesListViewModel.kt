package com.lelloman.read.ui.articleslist.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.lelloman.read.core.ResourceProvider
import com.lelloman.read.core.viewmodel.BaseViewModel
import com.lelloman.read.persistence.db.model.Article

abstract class ArticlesListViewModel(
    resourceProvider: ResourceProvider
) : BaseViewModel(resourceProvider) {

    abstract val isLoading: MutableLiveData<Boolean>

    abstract val articles: MutableLiveData<List<Article>>

    abstract fun refresh()

    abstract fun onSourcesClicked()
}
