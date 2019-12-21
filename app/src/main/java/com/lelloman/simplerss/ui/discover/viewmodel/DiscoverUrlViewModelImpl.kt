package com.lelloman.simplerss.ui.discover.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.lelloman.common.utils.LazyLiveData
import com.lelloman.common.utils.UrlValidator
import com.lelloman.simplerss.ui.OpenFoundFeedListScreenCommand
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
            emitCommand(OpenFoundFeedListScreenCommand(urlWithProtocol))
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