package com.lelloman.pdfscores.ui.pdfviewer

import android.arch.lifecycle.LiveData
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.pdfrenderer.PdfDocument

abstract class PdfViewerViewModel(dependencies: BaseViewModel.Dependencies)
    : BaseViewModel(dependencies) {

    abstract val pdfDocument: LiveData<PdfDocument>

    abstract fun setUri(uriString: String)
}