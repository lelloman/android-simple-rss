package com.lelloman.read.ui.sources.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.lelloman.read.core.ResourceProvider
import com.lelloman.read.core.viewmodel.BaseViewModel
import com.lelloman.read.ui.sources.repository.SourcesRepository

abstract class SourceViewModel(
    resourceProvider: ResourceProvider,
    protected val sourcesRepository: SourcesRepository
) : BaseViewModel(resourceProvider) {

    abstract val sourceName: MutableLiveData<String>
    abstract val sourceLastFetched: MutableLiveData<String>
    abstract val sourceUrl: MutableLiveData<String>

    abstract fun onSourceIdLoaded(sourceId: Long)
}