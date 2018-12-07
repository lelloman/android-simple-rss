package com.lelloman.pdfscores.ui.recentscores.viewmodel

import android.arch.lifecycle.LiveData
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.pdfscores.persistence.PdfScore
import com.lelloman.pdfscores.ui.recentscores.PdfScoreViewModelItem

abstract class RecentScoresViewModel(
    dependencies: BaseViewModel.Dependencies
) : BaseViewModel(dependencies) {

    abstract val recentScores: LiveData<List<PdfScoreViewModelItem>>
    abstract val progressVisible: LiveData<Boolean>

    abstract fun onPdfScoreClicked(pdfScore: PdfScore)
}