package com.lelloman.simplerss.ui.sources.viewmodel

import android.arch.lifecycle.MutableLiveData

class SourceViewModelImpl(
    private val sourcesRepository: com.lelloman.simplerss.ui.common.repository.SourcesRepository,
    dependencies: Dependencies
) : com.lelloman.simplerss.ui.sources.viewmodel.SourceViewModel(dependencies) {

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