package com.lelloman.simplerss.ui.discover.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.lelloman.common.viewmodel.BaseViewModel

abstract class FoundFeedListViewModel(dependencies: Dependencies) : BaseViewModel(dependencies) {

    abstract val isFindingFeeds: MutableLiveData<Boolean>

    abstract val foundFeeds: MutableLiveData<List<com.lelloman.simplerss.feed.finder.FoundFeed>>

    abstract fun onFoundFeedClicked(foundFeed: com.lelloman.simplerss.feed.finder.FoundFeed)

    abstract fun onAddAllClicked()

    abstract fun onAddAllFoundFeedsConfirmationClicked(foundFeeds: List<com.lelloman.simplerss.feed.finder.FoundFeed>)
}