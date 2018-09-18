package com.lelloman.read.ui.sources.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.view.View
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.read.persistence.db.model.Source

abstract class SourcesListViewModel(dependencies: Dependencies) : BaseViewModel(dependencies) {

    abstract val sources: MutableLiveData<List<Source>>

    abstract fun onFabClicked(view: View)

    abstract fun onSourceClicked(source: Source)

    abstract fun onSourceIsActiveChanged(sourceId: Long, isActive: Boolean)

    abstract fun onSourceSwiped(source: Source)
}