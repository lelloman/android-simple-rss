package com.lelloman.read.sources.viewmodel

import android.view.View
import com.lelloman.read.core.navigation.BackNavigationEvent

class AddSourceViewModelImpl : AddSourceViewModel() {

    override fun onCloseClicked(view: View) = navigate(BackNavigationEvent)

    override fun onSaveClicked(view: View) {
        navigate(BackNavigationEvent)
    }
}