package com.lelloman.pdfscores.persistence.assets

import android.content.res.AssetManager
import com.google.gson.GsonBuilder
import com.lelloman.pdfscores.persistence.model.Author
import com.lelloman.pdfscores.persistence.model.PdfScore
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

class AssetsPdfScoresProviderImpl(
    assetManager: AssetManager,
    ioScheduler: Scheduler
) : AssetsPdfScoresProvider {

    private val pdfScoresSubject = BehaviorSubject.create<List<PdfScore>>()
    private val authorsSubject = BehaviorSubject.create<List<Author>>()

    override val pdfScores: Observable<List<PdfScore>> = pdfScoresSubject.hide()
    override val authors: Observable<List<Author>> = authorsSubject.hide()

    init {
        val unused = Single
            .fromCallable {
                assetManager
                    .open(COLLECTION_JSON_FILE_NAME)
                    .readBytes()
                    .let {
                        gson.fromJson(String(it), PdfScoresCollection::class.java)
                    }
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
        const val COLLECTION_JSON_FILE_NAME = "collection.json"
        val gson by lazy { GsonBuilder().create() }
    }
}