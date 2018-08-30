package com.lelloman.read.ui.discover.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import android.view.View
import com.lelloman.read.core.ActionTokenProvider
import com.lelloman.read.core.ResourceProvider
import com.lelloman.read.core.di.qualifiers.IoScheduler
import com.lelloman.read.core.di.qualifiers.UiScheduler
import com.lelloman.read.core.navigation.NavigationScreen
import com.lelloman.read.core.navigation.ScreenNavigationEvent
import com.lelloman.read.ui.common.repository.DiscoverRepository
import com.lelloman.read.utils.LazyLiveData
import com.lelloman.read.utils.UrlValidator
import io.reactivex.Scheduler

class DiscoverUrlViewModelImpl(
    @UiScheduler private val uiScheduler: Scheduler,
    @IoScheduler private val ioScheduler: Scheduler,
    resourceProvider: ResourceProvider,
    actionTokenProvider: ActionTokenProvider,
    private val discoverRepository: DiscoverRepository,
    private val urlValidator: UrlValidator
) : DiscoverUrlViewModel(
    resourceProvider = resourceProvider,
    actionTokenProvider = actionTokenProvider
) {
    override val discoverUrl = ObservableField<String>()

    override val isFeedDiscoverLoading: MutableLiveData<Boolean> by LazyLiveData {
        subscription {
            discoverRepository
                .isFindingFeeds
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe(isFeedDiscoverLoading::postValue)
        }
    }

    override fun onDiscoverClicked(view: View?) {
        urlValidator.maybePrependProtocol(discoverUrl.get())?.let { urlWithProtocol ->
            discoverUrl.set(urlWithProtocol)
            discoverRepository.findFeeds(urlWithProtocol)
            navigate(ScreenNavigationEvent(NavigationScreen.FOUND_FEED_LIST, arrayOf(urlWithProtocol)))
        }
    }

    override fun onKeyboardActionDone() {
        onDiscoverClicked(null)
    }
}