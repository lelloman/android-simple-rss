package com.lelloman.simplerss.ui.articles.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ArticleViewModelImpl(dependencies: Dependencies) : ArticleViewModel(dependencies) {

    private val mutableProgressVisible = MutableLiveData<Boolean>()
    override val progressVisible: LiveData<Boolean> = mutableProgressVisible

    override fun onPageLoadingStateChanged(percent: Int) {
        mutableProgressVisible.postValue(percent != 100)
    }
}