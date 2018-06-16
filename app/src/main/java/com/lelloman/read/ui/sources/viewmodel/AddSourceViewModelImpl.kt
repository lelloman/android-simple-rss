package com.lelloman.read.ui.sources.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import com.lelloman.read.R
import com.lelloman.read.core.ResourceProvider

class AddSourceViewModelImpl(
    resourceProvider: ResourceProvider
) : AddSourceViewModel(resourceProvider) {

    override val sourceName = ObservableField<String>()
    override val sourceNameError = MutableLiveData<String>()

    override val sourceUrl = ObservableField<String>()
    override val sourceUrlError = MutableLiveData<String>()

    override fun onCloseClicked() = navigateBack()

    override fun onSaveClicked() {

        val hasValidName = with(sourceName.get()) { this != null && length > 2 }
        val hasValidUrl = with(sourceUrl.get()) { this != null && length > 2 }

        if (hasValidName && hasValidUrl) {
            // TODO insert into db
            navigateBack()
            sourceNameError.value = ""
            sourceUrlError.value = ""
        } else {
            val nameErrorValue = if (hasValidName) "" else getString(R.string.error_add_source_name)
            val urlErrorValue = if (hasValidUrl) "" else getString(R.string.error_add_source_url)

            sourceNameError.postValue(nameErrorValue)
            sourceUrlError.postValue(urlErrorValue)
        }
    }
}