package com.lelloman.simplerss.ui.sources.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.view.View
import com.lelloman.common.viewmodel.BaseViewModel

abstract class SourcesListViewModel(dependencies: Dependencies) : BaseViewModel(dependencies) {

    abstract val sources: MutableLiveData<List<com.lelloman.simplerss.persistence.db.model.Source>>

    abstract val emptyViewVisible: LiveData<Boolean>

    abstract fun onFabClicked(view: View)

    abstract fun onSourceClicked(source: com.lelloman.simplerss.persistence.db.model.Source)

    abstract fun onSourceIsActiveChanged(sourceId: Long, isActive: Boolean)

    abstract fun onSourceSwiped(source: com.lelloman.simplerss.persistence.db.model.Source)
}