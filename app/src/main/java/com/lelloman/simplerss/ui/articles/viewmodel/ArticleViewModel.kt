package com.lelloman.simplerss.ui.articles.viewmodel

import androidx.lifecycle.LiveData
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.common.webview.CookedWebView

abstract class ArticleViewModel(dependencies: Dependencies) :
    BaseViewModel(dependencies), CookedWebView.Listener {
    abstract val progressVisible: LiveData<Boolean>
}