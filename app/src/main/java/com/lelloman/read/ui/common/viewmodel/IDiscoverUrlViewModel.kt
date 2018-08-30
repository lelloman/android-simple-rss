package com.lelloman.read.ui.common.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import android.view.View
import com.lelloman.read.utils.OnKeyboardActionDoneListener

interface IDiscoverUrlViewModel : OnKeyboardActionDoneListener {
    abstract val discoverUrl: ObservableField<String>
    abstract val isFeedDiscoverLoading: MutableLiveData<Boolean>

    abstract fun onDiscoverClicked(view: View?)
}