package com.lelloman.read.sources.viewmodel

import android.view.View
import com.lelloman.read.core.BaseViewModel

abstract class AddSourceViewModel : BaseViewModel() {

    abstract fun onCloseClicked(view: View)

    abstract fun onSaveClicked(view: View)
}