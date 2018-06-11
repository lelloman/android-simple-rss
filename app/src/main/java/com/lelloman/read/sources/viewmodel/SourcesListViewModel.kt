package com.lelloman.read.sources.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.view.View
import com.lelloman.read.core.BaseViewModel
import com.lelloman.read.persistence.model.Source

abstract class SourcesListViewModel : BaseViewModel() {

    abstract val sources: MutableLiveData<List<Source>>

    abstract fun onFabClicked(view: View)

}