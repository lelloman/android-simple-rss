package com.lelloman.read.articleslist.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.lelloman.read.core.navigation.NavigationEvent
import com.lelloman.read.persistence.model.Article
import com.lelloman.read.utils.SingleLiveData

abstract class ArticlesListViewModel : ViewModel() {

    open val navigation = SingleLiveData<NavigationEvent>()

    abstract val isLoading: MutableLiveData<Boolean>

    abstract val articles: MutableLiveData<List<Article>>

    abstract fun refresh()

    abstract fun onSourcesClicked()
}
