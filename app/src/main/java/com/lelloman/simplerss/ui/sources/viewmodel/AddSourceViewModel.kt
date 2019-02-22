package com.lelloman.simplerss.ui.sources.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.lelloman.common.viewmodel.BaseViewModel

abstract class AddSourceViewModel(dependencies: Dependencies) : BaseViewModel(dependencies) {

    abstract val sourceName: ObservableField<String>
    abstract val sourceNameError: MutableLiveData<String>

    abstract val sourceUrl: ObservableField<String>
    abstract val sourceUrlError: MutableLiveData<String>
    abstract val sourceUrlDrawable: MutableLiveData<Int>

    abstract val testingUrl: MutableLiveData<Boolean>


    abstract fun onSaveClicked()

    abstract fun onTestUrlClicked()
}