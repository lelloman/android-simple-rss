package com.lelloman.pdfscores.ui.pdfscoreslist.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.lelloman.common.navigation.DeepLink
import com.lelloman.common.utils.LazyLiveData
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.pdfscores.persistence.PdfScoresRepository
import com.lelloman.pdfscores.persistence.model.PdfScore
import com.lelloman.pdfscores.ui.PdfScoresScreen
import com.lelloman.pdfscores.ui.PdfScoresScreen.Companion.EXTRA_PDF_URI
import com.lelloman.pdfscores.ui.pdfscoreslist.PdfScoreViewModelItem
import io.reactivex.Scheduler

class PdfScoresListViewModelImpl(
    dependencies: BaseViewModel.Dependencies,
    private val pdfScoresRepository: PdfScoresRepository,
    private val ioScheduler: Scheduler
) : PdfScoresListViewModel(dependencies) {

    override val recentScores: MutableLiveData<List<PdfScoreViewModelItem>> by LazyLiveData {
        subscription {
            pdfScoresRepository
                .getScores()
                .map {scores ->
                    scores.map(::PdfScoreViewModelItem)
                }
                .subscribeOn(ioScheduler)
                .subscribe {
                    recentScores.postValue(it)
                }
        }
    }

    override val progressVisible: MutableLiveData<Boolean> by LazyLiveData {
        progressVisible.postValue(true)
    }

    override fun onPdfScoreClicked(pdfScore: PdfScore) {
        DeepLink(PdfScoresScreen.PDF_VIEWER)
            .putString(EXTRA_PDF_URI, pdfScore.uri)
            .let(::navigate)
    }
}