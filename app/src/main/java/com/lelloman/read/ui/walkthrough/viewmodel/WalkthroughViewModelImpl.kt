package com.lelloman.read.ui.walkthrough.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import android.os.Bundle
import android.view.View
import com.lelloman.read.core.ActionTokenProvider
import com.lelloman.read.core.ResourceProvider
import com.lelloman.read.core.di.qualifiers.IoScheduler
import com.lelloman.read.core.di.qualifiers.UiScheduler
import com.lelloman.read.core.navigation.NavigationScreen
import com.lelloman.read.core.navigation.ScreenAndCloseNavigationEvent
import com.lelloman.read.core.navigation.ScreenNavigationEvent
import com.lelloman.read.persistence.settings.AppSettings
import com.lelloman.read.ui.common.WalkthroughRepository
import com.lelloman.read.utils.LazyLiveData
import com.lelloman.read.utils.UrlValidator
import io.reactivex.Scheduler

class WalkthroughViewModelImpl(
    @UiScheduler private val uiScheduler: Scheduler,
    @IoScheduler private val ioScheduler: Scheduler,
    resourceProvider: ResourceProvider,
    actionTokenProvider: ActionTokenProvider,
    private val appSettings: AppSettings,
    private val walkthroughRepository: WalkthroughRepository,
    private val urlValidator: UrlValidator
) : WalkthroughViewModel(
    resourceProvider = resourceProvider,
    actionTokenProvider = actionTokenProvider
) {

    override val discoverUrl = ObservableField<String>()

    override val isFeedDiscoverLoading: MutableLiveData<Boolean> by LazyLiveData {
        subscription {
            walkthroughRepository
                .isFindingFeeds
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe(isFeedDiscoverLoading::postValue)
        }
    }

    override fun onSaveInstanceState(bundle: Bundle) {
        super.onSaveInstanceState(bundle)
        bundle.putString(ARG_URL, discoverUrl.get())
    }

    override fun onRestoreInstanceState(bundle: Bundle) {
        super.onRestoreInstanceState(bundle)
        bundle.getString(ARG_URL)?.let { discoverUrl.set(it) }
    }

    override fun onSkipClicked(view: View) {
        appSettings.setShouldShowWalkthtough(false)
        navigate(ScreenAndCloseNavigationEvent(NavigationScreen.ARTICLES_LIST))
    }

    override fun onKeyboardActionDone() {
        onDiscoverClicked(null)
    }

    override fun onDiscoverClicked(view: View?) {
        urlValidator.maybePrependProtocol(discoverUrl.get())?.let { urlWithProtocol ->
            discoverUrl.set(urlWithProtocol)
            walkthroughRepository.findFeeds(urlWithProtocol)
            navigate(ScreenNavigationEvent(NavigationScreen.FOUND_FEED_LIST, arrayOf(urlWithProtocol)))

//            animate(DiscoverUrlSelectedAnimationEvent)
//            foundFeedsInternal.clear()
//            foundFeeds.postValue(foundFeedsInternal)
//            subscription {
//                feedFinder
//                    .findValidFeedUrls(urlWithProtocol)
//                    .subscribeOn(ioScheduler)
//                    .observeOn(uiScheduler)
//                    .subscribe {
//                        foundFeedsInternal.add(it)
//                        foundFeeds.postValue(ArrayList(foundFeedsInternal))
//                    }
//            }
        }
    }

    private companion object {
        const val ARG_URL = "Url"
    }
}