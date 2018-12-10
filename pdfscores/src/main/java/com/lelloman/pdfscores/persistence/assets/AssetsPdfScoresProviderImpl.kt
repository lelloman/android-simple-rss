package com.lelloman.pdfscores.persistence.assets

import android.content.res.AssetFileDescriptor
import com.google.gson.GsonBuilder
import com.lelloman.pdfscores.persistence.model.Author
import com.lelloman.pdfscores.persistence.model.PdfScore
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

class AssetsPdfScoresProviderImpl(
    assetFileDescriptor: AssetFileDescriptor,
    ioScheduler: Scheduler
) : AssetsPdfScoresProvider {

    private val pdfScoresSubject = BehaviorSubject.create<List<PdfScore>>()
    private val authorsSubject = BehaviorSubject.create<List<Author>>()

    override val pdfScores: Observable<List<PdfScore>> = pdfScoresSubject.hide()
    override val authors: Observable<List<Author>> = authorsSubject.hide()

    init {
        val unused = Single
            .fromCallable {
                val jsonString = assetFileDescriptor
                    .createInputStream()
                    .bufferedReader()
                    .readText()
                gson.fromJson(jsonString, PdfScoresCollection::class.java)
            }
            .subscribeOn(ioScheduler)
            .observeOn(ioScheduler)
            .doOnError {
                TODO("log this")
            }
            .onErrorReturn {
                PdfScoresCollection(
                    version = -1,
                    authors = emptyList(),
                    pdfScores = emptyList()
                )
            }
            .subscribe({
                pdfScoresSubject.onNext(it.pdfScores)
                authorsSubject.onNext(it.authors)
            }, {
                TODO("log it...?")
            })

    }

    private companion object {
        val gson by lazy { GsonBuilder().create() }
    }
}