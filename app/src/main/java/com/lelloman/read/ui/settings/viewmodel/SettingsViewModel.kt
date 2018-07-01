package com.lelloman.read.ui.settings.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import com.lelloman.read.core.ResourceProvider
import com.lelloman.read.core.viewmodel.BaseViewModel
import com.lelloman.read.persistence.settings.SourceRefreshInterval

abstract class SettingsViewModel(
    resourceProvider: ResourceProvider
) : BaseViewModel(resourceProvider) {

    abstract val minRefreshIntervals: MutableLiveData<List<String>>
    abstract val selectedMinRefreshInterval: ObservableField<Int>

    abstract val articlesListImagesSelected: MutableLiveData<Boolean>

    abstract val useMeteredNetworkSelected: MutableLiveData<Boolean>

    abstract val openArticlesInAppSelected: MutableLiveData<Boolean>

    abstract fun onSourceRefreshMinIntervalSelected(interval: SourceRefreshInterval)

    abstract fun onArticleListImagesChanged(isActive: Boolean)

    abstract fun onUseMeteredNetworkChanged(isActive: Boolean)

    abstract fun onOpenArticlesInAppChanged(isActive: Boolean)
}