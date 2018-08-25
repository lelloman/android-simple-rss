package com.lelloman.read.ui.walkthrough.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import android.view.View
import com.lelloman.read.core.ActionTokenProvider
import com.lelloman.read.core.ResourceProvider
import com.lelloman.read.core.viewmodel.BaseViewModel
import com.lelloman.read.utils.OnKeyboardActionDoneListener

abstract class WalkthroughViewModel(
    resourceProvider: ResourceProvider,
    actionTokenProvider: ActionTokenProvider
) : BaseViewModel(
    resourceProvider = resourceProvider,
    actionTokenProvider = actionTokenProvider
), OnKeyboardActionDoneListener {

    abstract fun onSkipClicked(view: View)

    // discover page
    abstract val discoverUrl: ObservableField<String>
    abstract val isFeedDiscoverLoading: MutableLiveData<Boolean>

    abstract fun onDiscoverClicked(view: View?)
    // discover page
}