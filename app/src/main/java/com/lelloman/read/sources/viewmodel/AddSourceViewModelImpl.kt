package com.lelloman.read.sources.viewmodel

class AddSourceViewModelImpl : AddSourceViewModel() {

    override fun onCloseClicked() = navigateBack()

    override fun onSaveClicked() = navigateBack()
}