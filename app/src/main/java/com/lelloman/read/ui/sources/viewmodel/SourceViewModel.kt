package com.lelloman.read.ui.sources.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.lelloman.common.view.ResourceProvider
import com.lelloman.read.core.viewmodel.BaseViewModel

abstract class SourceViewModel(
    resourceProvider: ResourceProvider
) : BaseViewModel(resourceProvider) {

    abstract val sourceName: MutableLiveData<String>
    abstract val sourceLastFetched: MutableLiveData<String>
    abstract val sourceUrl: MutableLiveData<String>

    abstract fun onSourceIdLoaded(sourceId: Long)
}