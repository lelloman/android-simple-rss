package com.lelloman.pdfscores.persistence

import com.lelloman.pdfscores.persistence.model.Author
import com.lelloman.pdfscores.persistence.model.PdfScore
import io.reactivex.Flowable

interface PdfScoresRepository {

    fun getScores(): Flowable<List<PdfScore>>

    fun getAuthors(): Flowable<List<Author>>
}