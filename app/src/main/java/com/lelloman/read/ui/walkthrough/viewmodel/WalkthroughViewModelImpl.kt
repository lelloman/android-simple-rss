package com.lelloman.read.ui.walkthrough.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import android.view.View
import com.lelloman.read.core.ActionTokenProvider
import com.lelloman.read.core.ResourceProvider
import com.lelloman.read.core.di.qualifiers.IoScheduler
import com.lelloman.read.core.di.qualifiers.UiScheduler
import com.lelloman.read.core.navigation.NavigationScreen
import com.lelloman.read.core.navigation.ScreenAndCloseNavigationEvent
import com.lelloman.read.feed.finder.FeedFinder
import com.lelloman.read.feed.finder.FoundFeed
import com.lelloman.read.persistence.settings.AppSettings
import com.lelloman.read.utils.LazyLiveData
import com.lelloman.read.utils.UrlValidator
import io.reactivex.Observable
import io.reactivex.Scheduler
import java.util.concurrent.TimeUnit

class WalkthroughViewModelImpl(
    @UiScheduler private val uiScheduler: Scheduler,
    @IoScheduler private val ioScheduler: Scheduler,
    resourceProvider: ResourceProvider,
    actionTokenProvider: ActionTokenProvider,
    private val appSettings: AppSettings,
    private val feedFinder: FeedFinder,
    private val urlValidator: UrlValidator
) : WalkthroughViewModel(
    resourceProvider = resourceProvider,
    actionTokenProvider = actionTokenProvider
) {

    override val discoverUrl = ObservableField<String>()

    override val isFeedDiscoverLoading: MutableLiveData<Boolean> by LazyLiveData {
        subscription {
            feedFinder
                .loading
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe(isFeedDiscoverLoading::postValue)
        }
    }

    private val foundFeedsInternal = mutableListOf<FoundFeed>()

    override val foundFeeds: MutableLiveData<List<FoundFeed>> by LazyLiveData {
        foundFeeds.postValue(foundFeedsInternal)
    }

    override fun onSkipClicked(view: View) {
        appSettings.setShouldShowWalkthtough(false)
        navigate(ScreenAndCloseNavigationEvent(NavigationScreen.ARTICLES_LIST))
    }

    override fun onDiscoverClicked(view: View) {
        urlValidator.maybePrependProtocol(discoverUrl.get())?.let { urlWithProtocol ->
            discoverUrl.set(urlWithProtocol)
            animate(DiscoverUrlSelectedAnimationEvent)
            foundFeedsInternal.clear()
            foundFeeds.postValue(foundFeedsInternal)
            subscription {
//                val founds = Array(50) {
//                    FoundFeed(it.toLong(), "aaaaaaaaaaaaaaaa", 400, "aaaaaaaaaaaaa")
//                }
//                Observable
//                    .fromIterable(founds.toList())
//                    .delay(1, TimeUnit.SECONDS)
//                    .subscribeOn(ioScheduler)
//                    .observeOn(uiScheduler)
//                    .subscribe {
//                        foundFeedsInternal.add( it)
//                        foundFeeds.postValue(ArrayList(foundFeedsInternal))
//                    }
                feedFinder
                    .findValidFeedUrls(urlWithProtocol)
                    .subscribeOn(ioScheduler)
                    .observeOn(uiScheduler)
                    .subscribe {
                        foundFeedsInternal.add( it)
                        foundFeeds.postValue(ArrayList(foundFeedsInternal))
                    }
            }
        }
    }
}