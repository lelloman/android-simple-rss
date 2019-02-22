package com.lelloman.simplerss.ui.common.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.lelloman.simplerss.utils.OnKeyboardActionDoneListener

interface IDiscoverUrlViewModel : OnKeyboardActionDoneListener {
    val discoverUrl: ObservableField<String>
    val isFeedDiscoverLoading: MutableLiveData<Boolean>

    fun onDiscoverClicked()
}