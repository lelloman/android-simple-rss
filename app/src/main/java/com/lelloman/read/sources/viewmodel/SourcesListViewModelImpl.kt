package com.lelloman.read.sources.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.view.View
import com.lelloman.read.core.di.qualifiers.IoScheduler
import com.lelloman.read.core.di.qualifiers.UiScheduler
import com.lelloman.read.core.navigation.NavigationScreen
import com.lelloman.read.core.navigation.ScreenNavigationEvent
import com.lelloman.read.persistence.model.Source
import com.lelloman.read.sources.repository.SourcesRepository
import com.lelloman.read.utils.LazyLiveData
import io.reactivex.Scheduler

class SourcesListViewModelImpl(
    @IoScheduler private val ioScheduler: Scheduler,
    @UiScheduler private val uiScheduler: Scheduler,
    private val sourcesRepository: SourcesRepository
) : SourcesListViewModel() {

    override val sources: MutableLiveData<List<Source>> by LazyLiveData({
        subscription {
            sourcesRepository.fetchSources()
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe { sources.value = it }
        }
    })

    override fun onFabClicked(view: View) =
        navigate(ScreenNavigationEvent(NavigationScreen.ADD_SOURCE))
}