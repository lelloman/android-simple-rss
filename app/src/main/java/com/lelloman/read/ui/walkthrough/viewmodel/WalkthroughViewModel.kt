package com.lelloman.read.ui.walkthrough.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import android.view.View
import com.lelloman.read.core.ActionTokenProvider
import com.lelloman.read.core.ResourceProvider
import com.lelloman.read.core.viewmodel.BaseViewModel
import com.lelloman.read.feed.finder.FoundFeed

abstract class WalkthroughViewModel(
    resourceProvider: ResourceProvider,
    actionTokenProvider: ActionTokenProvider
) : BaseViewModel(
    resourceProvider = resourceProvider,
    actionTokenProvider = actionTokenProvider
) {

    abstract fun onSkipClicked(view: View)

    // discover page
    abstract val discoverUrl: ObservableField<String>
    abstract val isFeedDiscoverLoading: MutableLiveData<Boolean>
    abstract val foundFeeds: MutableLiveData<List<FoundFeed>>

    abstract fun onDiscoverClicked(view: View)
    abstract fun onFoundFeedClicked(foundFeed: FoundFeed)
    // discover page
}