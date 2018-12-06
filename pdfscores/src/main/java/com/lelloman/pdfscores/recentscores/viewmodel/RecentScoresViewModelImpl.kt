package com.lelloman.pdfscores.recentscores.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.lelloman.common.utils.LazyLiveData
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.pdfscores.persistence.PdfScoresDao
import com.lelloman.pdfscores.recentscores.PdfScoreViewModelItem
import io.reactivex.Scheduler

class RecentScoresViewModelImpl(
    dependencies: BaseViewModel.Dependencies,
    private val pdfScoresDao: PdfScoresDao,
    private val ioScheduler: Scheduler
) : RecentScoresViewModel(dependencies) {

    override val recentScores: MutableLiveData<List<PdfScoreViewModelItem>> by LazyLiveData {
        subscription {
            pdfScoresDao
                .getAll()
                .map {
                    it.map {
                        PdfScoreViewModelItem(it)
                    }
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
}