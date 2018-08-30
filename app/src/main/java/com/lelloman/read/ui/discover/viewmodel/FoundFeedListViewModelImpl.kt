package com.lelloman.read.ui.discover.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.lelloman.read.core.ActionTokenProvider
import com.lelloman.read.core.ResourceProvider
import com.lelloman.read.core.di.qualifiers.IoScheduler
import com.lelloman.read.core.di.qualifiers.UiScheduler
import com.lelloman.read.core.navigation.NavigationScreen
import com.lelloman.read.core.navigation.ScreenNavigationEvent
import com.lelloman.read.feed.finder.FoundFeed
import com.lelloman.read.ui.common.repository.DiscoverRepository
import com.lelloman.read.utils.LazyLiveData
import io.reactivex.Scheduler

class FoundFeedListViewModelImpl(
    @UiScheduler uiScheduler: Scheduler,
    @IoScheduler private val ioScheduler: Scheduler,
    private val discoverRepository: DiscoverRepository,
    resourceProvider: ResourceProvider,
    actionTokenProvider: ActionTokenProvider
) : FoundFeedListViewModel(
    resourceProvider = resourceProvider,
    actionTokenProvider = actionTokenProvider
) {

    override val isFindingFeeds: MutableLiveData<Boolean> by LazyLiveData {
        subscription {
            discoverRepository
                .isFindingFeeds
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe {
                    isFindingFeeds.postValue(it)
                }
        }
    }

    override val foundFeeds: MutableLiveData<List<FoundFeed>> by LazyLiveData {
        subscription {
            discoverRepository
                .foundFeeds
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe { foundFeeds.postValue(ArrayList(it)) }
        }
    }

    override fun onFoundFeedClicked(foundFeed: FoundFeed) {
        navigate(
            ScreenNavigationEvent(
                NavigationScreen.ADD_SOURCE,
                arrayOf(
                    foundFeed.name ?: foundFeed.url,
                    foundFeed.url
                )
            )
        )
    }

    override fun onAddAllClicked() {
        foundFeeds
            .value
            ?.let { names ->
                navigate(
                    ScreenNavigationEvent(
                        NavigationScreen.ADD_FOUND_FEEDS_CONFIRMATION,
                        arrayOf(names)
                    )
                )
            }
    }

    override fun onAddAllFoundFeedsConfirmationClicked(foundFeeds: List<FoundFeed>) {
        discoverRepository
            .addFoundFeeds(foundFeeds)
            .subscribeOn(ioScheduler)
            .subscribe()
        navigateBack()
    }
}