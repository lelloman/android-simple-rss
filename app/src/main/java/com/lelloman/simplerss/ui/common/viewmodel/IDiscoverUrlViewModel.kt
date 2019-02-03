package com.lelloman.simplerss.ui.common.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import android.view.View
import com.lelloman.simplerss.utils.OnKeyboardActionDoneListener

interface IDiscoverUrlViewModel : OnKeyboardActionDoneListener {
    val discoverUrl: ObservableField<String>
    val isFeedDiscoverLoading: MutableLiveData<Boolean>

    fun onDiscoverClicked(view: View?)
}