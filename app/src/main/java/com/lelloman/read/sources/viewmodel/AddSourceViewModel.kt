package com.lelloman.read.sources.viewmodel

import com.lelloman.read.core.BaseViewModel

abstract class AddSourceViewModel : BaseViewModel() {

    abstract fun onCloseClicked()

    abstract fun onSaveClicked()
}