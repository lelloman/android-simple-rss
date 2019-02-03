package com.lelloman.simplerss.ui.sources.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.view.View
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.simplerss.persistence.db.model.Source

abstract class SourcesListViewModel(dependencies: Dependencies) : BaseViewModel(dependencies) {

    abstract val sources: MutableLiveData<List<Source>>

    abstract val emptyViewVisible: LiveData<Boolean>

    abstract fun onFabClicked(view: View)

    abstract fun onSourceClicked(source: Source)

    abstract fun onSourceIsActiveChanged(sourceId: Long, isActive: Boolean)

    abstract fun onSourceSwiped(source: Source)
}