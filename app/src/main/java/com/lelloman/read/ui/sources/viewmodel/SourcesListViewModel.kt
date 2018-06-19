package com.lelloman.read.ui.sources.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.view.View
import com.lelloman.read.core.ResourceProvider
import com.lelloman.read.core.viewmodel.BaseViewModel
import com.lelloman.read.persistence.db.model.Source

abstract class SourcesListViewModel(
    resourceProvider: ResourceProvider
) : BaseViewModel(resourceProvider) {

    abstract val sources: MutableLiveData<List<Source>>

    abstract fun onFabClicked(view: View)

    abstract fun onSourceClicked(sourceId: Long)

    abstract fun onSourceIsActiveChanged(sourceId: Long, isActive: Boolean)
}