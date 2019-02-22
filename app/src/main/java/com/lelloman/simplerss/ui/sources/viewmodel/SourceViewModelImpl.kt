package com.lelloman.simplerss.ui.sources.viewmodel

import androidx.lifecycle.MutableLiveData
import com.lelloman.simplerss.ui.common.repository.SourcesRepository

class SourceViewModelImpl(
    private val sourcesRepository: SourcesRepository,
    dependencies: Dependencies
) : SourceViewModel(dependencies) {

    override val sourceName = MutableLiveData<String>()
    override val sourceLastFetched = MutableLiveData<String>()
    override val sourceUrl = MutableLiveData<String>()

    private var sourceId = 0L

    override fun onSourceIdLoaded(sourceId: Long) {
        if (sourceId == this.sourceId) {
            return
        }
        this.sourceId = sourceId

        subscription {
            sourcesRepository
                .getSource(sourceId)
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe {
                    sourceName.value = it.name
                    sourceUrl.value = it.url
                    sourceLastFetched.value = ""//semanticTimeProvider.getSourceLastFetchedString(it)
                }
        }
    }
}