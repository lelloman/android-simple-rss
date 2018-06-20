package com.lelloman.read.ui.sources.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import com.lelloman.read.R
import com.lelloman.read.core.ResourceProvider
import com.lelloman.read.persistence.db.model.Source
import com.lelloman.read.ui.sources.repository.SourcesRepository
import io.reactivex.Scheduler

class AddSourceViewModelImpl(
    resourceProvider: ResourceProvider,
    private val sourcesRepository: SourcesRepository,
    private val ioScheduler: Scheduler,
    private val uiScheduler: Scheduler
) : AddSourceViewModel(resourceProvider) {

    override val sourceName = ObservableField<String>()
    override val sourceNameError = MutableLiveData<String>()

    override val sourceUrl = ObservableField<String>()
    override val sourceUrlError = MutableLiveData<String>()

    private var saving = false

    override fun onCloseClicked() = navigateBack()

    override fun onSaveClicked() {
        if (saving) return

        saving = true

        val name = sourceName.get()
        val url = sourceUrl.get()
        val hasValidName = name != null && name.length > 2
        val hasValidUrl = url != null && url.length > 2

        if (hasValidName && hasValidUrl) {
            navigateBack()
            sourceNameError.value = ""
            sourceUrlError.value = ""
            sourcesRepository
                .insertSource(Source(
                    name = name!!,
                    url = url!!,
                    isActive = true
                ))
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .doAfterTerminate { saving = false }
                .subscribe({
                    navigateBack()
                }, {
                    shortToast(R.string.something_went_wrong)
                })
        } else {
            val nameErrorValue = if (hasValidName) "" else getString(R.string.error_add_source_name)
            val urlErrorValue = if (hasValidUrl) "" else getString(R.string.error_add_source_url)

            sourceNameError.postValue(nameErrorValue)
            sourceUrlError.postValue(urlErrorValue)
        }
    }
}