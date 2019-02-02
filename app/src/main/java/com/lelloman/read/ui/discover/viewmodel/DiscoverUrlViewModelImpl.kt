package com.lelloman.read.ui.discover.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import android.view.View
import com.lelloman.common.navigation.DeepLink
import com.lelloman.common.utils.LazyLiveData
import com.lelloman.common.utils.UrlValidator
import com.lelloman.read.navigation.ReadNavigationScreen
import com.lelloman.read.navigation.ReadNavigationScreen.Companion.ARG_URL
import com.lelloman.read.ui.common.repository.DiscoverRepository

class DiscoverUrlViewModelImpl(
    private val discoverRepository: DiscoverRepository,
    private val urlValidator: UrlValidator,
    dependencies: Dependencies
) : DiscoverUrlViewModel(dependencies) {
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
            navigate(
                DeepLink(ReadNavigationScreen.FOUND_FEED_LIST)
                    .putString(ARG_URL, urlWithProtocol)
            )
        }
    }

    override fun onKeyboardActionDone() {
        onDiscoverClicked(null)
    }
}