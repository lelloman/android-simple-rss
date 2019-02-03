package com.lelloman.simplerss.ui.discover.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import com.lelloman.common.navigation.DeepLink
import com.lelloman.common.utils.LazyLiveData
import com.lelloman.common.utils.UrlValidator
import com.lelloman.simplerss.navigation.SimpleRssNavigationScreen
import com.lelloman.simplerss.navigation.SimpleRssNavigationScreen.Companion.ARG_URL
import com.lelloman.simplerss.ui.common.repository.DiscoverRepository

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

    override fun onDiscoverClicked() {
        urlValidator.maybePrependProtocol(discoverUrl.get())?.let { urlWithProtocol ->
            discoverUrl.set(urlWithProtocol)
            discoverRepository.findFeeds(urlWithProtocol)
            navigate(
                DeepLink(SimpleRssNavigationScreen.FOUND_FEED_LIST)
                    .putString(ARG_URL, urlWithProtocol)
            )
        }
    }

    override fun onViewShown() {
        super.onViewShown()
        discoverRepository.reset()
    }

    override fun onKeyboardActionDone() {
        onDiscoverClicked()
    }
}