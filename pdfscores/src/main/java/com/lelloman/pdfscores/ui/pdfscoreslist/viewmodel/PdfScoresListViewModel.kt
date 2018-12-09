package com.lelloman.pdfscores.ui.pdfscoreslist.viewmodel

import android.arch.lifecycle.LiveData
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.pdfscores.persistence.model.PdfScore
import com.lelloman.pdfscores.ui.pdfscoreslist.PdfScoreViewModelItem

abstract class PdfScoresListViewModel(
    dependencies: BaseViewModel.Dependencies
) : BaseViewModel(dependencies) {

    abstract val recentScores: LiveData<List<PdfScoreViewModelItem>>
    abstract val progressVisible: LiveData<Boolean>

    abstract fun onPdfScoreClicked(pdfScore: PdfScore)
}