package com.lelloman.pdfscores.recentscores.viewmodel

import android.arch.lifecycle.LiveData
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.pdfscores.recentscores.PdfScoreViewModelItem

abstract class RecentScoresViewModel(
    dependencies: BaseViewModel.Dependencies
) : BaseViewModel(dependencies) {

    abstract val recentScores: LiveData<List<PdfScoreViewModelItem>>
    abstract val progressVisible: LiveData<Boolean>
}