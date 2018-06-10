package com.lelloman.read.sourceslist.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.view.View
import com.lelloman.read.core.di.qualifiers.IoScheduler
import com.lelloman.read.core.di.qualifiers.UiScheduler
import com.lelloman.read.persistence.model.Source
import com.lelloman.read.sourceslist.repository.SourcesRepository
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import java.util.*
import javax.inject.Inject

class SourcesListViewModel @Inject constructor(
    @IoScheduler private val ioScheduler: Scheduler,
    @UiScheduler private val uiScheduler: Scheduler,
    private val sourcesRepository: SourcesRepository
) : ViewModel() {

    private val subscriptions = CompositeDisposable()
    private val random = Random()

    val sources by lazy {
        MutableLiveData<List<Source>>().also { subscribeSources() }
    }

    private fun subscribeSources() {
        subscriptions.add(
            sourcesRepository.fetchSources()
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe { sources.value = it }
        )
    }

    fun onFabClicked(view: View) {
        sourcesRepository
            .insertSource(Source(
                0L,
                "Source ${random.nextInt()}",
                "www.staceppa.com")
            )
            .subscribeOn(ioScheduler)
            .subscribe()
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }
}