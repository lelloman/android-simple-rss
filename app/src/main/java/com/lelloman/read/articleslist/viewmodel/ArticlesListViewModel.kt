package com.lelloman.read.articleslist.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.lelloman.read.persistence.model.Article

abstract class ArticlesListViewModel : ViewModel() {

    abstract val isLoading: MutableLiveData<Boolean>

    abstract val articles: MutableLiveData<List<Article>>

    abstract fun refresh()
}
