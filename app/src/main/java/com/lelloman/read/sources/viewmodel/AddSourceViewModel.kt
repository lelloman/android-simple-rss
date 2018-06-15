package com.lelloman.read.sources.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import com.lelloman.read.core.BaseViewModel
import com.lelloman.read.core.ResourceProvider

abstract class AddSourceViewModel(
    resourceProvider: ResourceProvider
) : BaseViewModel(resourceProvider) {

    abstract val sourceName: ObservableField<String>
    abstract val sourceNameError: MutableLiveData<String>

    abstract val sourceUrl: ObservableField<String>
    abstract val sourceUrlError: MutableLiveData<String>

    abstract fun onCloseClicked()

    abstract fun onSaveClicked()
}