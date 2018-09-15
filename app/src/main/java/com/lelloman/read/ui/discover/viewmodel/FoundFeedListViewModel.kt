package com.lelloman.read.ui.discover.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.lelloman.common.utils.ActionTokenProvider
import com.lelloman.common.view.ResourceProvider
import com.lelloman.read.core.viewmodel.BaseViewModel
import com.lelloman.read.feed.finder.FoundFeed

abstract class FoundFeedListViewModel(
    resourceProvider: ResourceProvider,
    actionTokenProvider: ActionTokenProvider
) : BaseViewModel(
    actionTokenProvider = actionTokenProvider,
    resourceProvider = resourceProvider
) {

    abstract val isFindingFeeds: MutableLiveData<Boolean>

    abstract val foundFeeds: MutableLiveData<List<FoundFeed>>

    abstract fun onFoundFeedClicked(foundFeed: FoundFeed)

    abstract fun onAddAllClicked()

    abstract fun onAddAllFoundFeedsConfirmationClicked(foundFeeds: List<FoundFeed>)
}