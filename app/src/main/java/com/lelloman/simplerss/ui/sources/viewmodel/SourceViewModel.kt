package com.lelloman.simplerss.ui.sources.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.lelloman.common.viewmodel.BaseViewModel

abstract class SourceViewModel(dependencies: Dependencies) : BaseViewModel(dependencies) {

    abstract val sourceName: MutableLiveData<String>
    abstract val sourceLastFetched: MutableLiveData<String>
    abstract val sourceUrl: MutableLiveData<String>

    abstract fun onSourceIdLoaded(sourceId: Long)
}