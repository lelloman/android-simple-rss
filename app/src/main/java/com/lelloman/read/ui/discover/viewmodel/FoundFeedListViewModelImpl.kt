package com.lelloman.read.ui.discover.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.lelloman.common.di.qualifiers.IoScheduler
import com.lelloman.common.di.qualifiers.UiScheduler
import com.lelloman.common.navigation.DeepLink
import com.lelloman.common.utils.ActionTokenProvider
import com.lelloman.common.utils.LazyLiveData
import com.lelloman.common.view.ResourceProvider
import com.lelloman.read.core.navigation.ReadNavigationScreen
import com.lelloman.read.core.navigation.ReadNavigationScreen.Companion.ARG_FOUND_FEEDS
import com.lelloman.read.core.navigation.ReadNavigationScreen.Companion.ARG_SOURCE_NAME
import com.lelloman.read.core.navigation.ReadNavigationScreen.Companion.ARG_SOURCE_URL
import com.lelloman.read.feed.finder.FoundFeed
import com.lelloman.read.ui.common.repository.DiscoverRepository
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
            DeepLink(ReadNavigationScreen.ADD_SOURCE)
                .putString(ARG_SOURCE_NAME, foundFeed.name ?: foundFeed.url)
                .putString(ARG_SOURCE_URL, foundFeed.url)
        )
    }

    override fun onAddAllClicked() {
        foundFeeds
            .value
            ?.let { foundFeedsList ->
                val foundFeeds = foundFeedsList as? ArrayList ?: ArrayList(foundFeedsList)
                navigate(
                    DeepLink(ReadNavigationScreen.ADD_FOUND_FEEDS_CONFIRMATION)
                        .putSerializableArrayList(ARG_FOUND_FEEDS, foundFeeds)
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