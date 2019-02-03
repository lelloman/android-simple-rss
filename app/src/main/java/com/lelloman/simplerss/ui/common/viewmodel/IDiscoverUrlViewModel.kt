package com.lelloman.simplerss.ui.common.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import android.view.View

interface IDiscoverUrlViewModel : com.lelloman.simplerss.utils.OnKeyboardActionDoneListener {
    val discoverUrl: ObservableField<String>
    val isFeedDiscoverLoading: MutableLiveData<Boolean>

    fun onDiscoverClicked(view: View?)
}