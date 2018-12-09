package com.lelloman.pdfscores.persistence.assets

import com.lelloman.pdfscores.persistence.model.Author
import com.lelloman.pdfscores.persistence.model.PdfScore
import io.reactivex.Observable

interface AssetsPdfScoresProvider {

    val pdfScores: Observable<List<PdfScore>>

    val authors: Observable<List<Author>>

    companion object {
        const val COLLECTION_JSON_FILE_NAME = "collection.json"
    }
}