package com.lelloman.read.ui.sources.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.lelloman.read.core.ResourceProvider
import com.lelloman.read.core.TimeDiffCalculator
import com.lelloman.read.ui.sources.repository.SourcesRepository
import io.reactivex.Scheduler

class SourceViewModelImpl(
    private val ioScheduler: Scheduler,
    private val uiScheduler: Scheduler,
    private val timeDiffCalculator: TimeDiffCalculator,
    resourceProvider: ResourceProvider,
    sourcesRepository: SourcesRepository
) : SourceViewModel(
    resourceProvider = resourceProvider,
    sourcesRepository = sourcesRepository
) {

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
                    sourceLastFetched.value = timeDiffCalculator.getSourceLastFetchedString(it)
                }
        }
    }
}