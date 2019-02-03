package com.lelloman.simplerss.ui.settings.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.simplerss.persistence.settings.SourceRefreshInterval

abstract class SettingsViewModel(dependencies: Dependencies) : BaseViewModel(dependencies) {

    abstract val minRefreshIntervals: MutableLiveData<List<String>>
    abstract val selectedMinRefreshInterval: ObservableField<Int>

    abstract val themes: MutableLiveData<List<String>>
    abstract val selectedTheme: ObservableField<Int>

    abstract val articlesListImagesSelected: MutableLiveData<Boolean>

    abstract val useMeteredNetworkSelected: MutableLiveData<Boolean>

    abstract val openArticlesInAppSelected: MutableLiveData<Boolean>

    abstract fun onSourceRefreshMinIntervalSelected(interval: SourceRefreshInterval)

    abstract fun onArticleListImagesChanged(isActive: Boolean)

    abstract fun onUseMeteredNetworkChanged(isActive: Boolean)

    abstract fun onOpenArticlesInAppChanged(isActive: Boolean)
}