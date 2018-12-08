package com.lelloman.pdfscores.persistence.assets

import com.lelloman.pdfscores.persistence.model.Author
import com.lelloman.pdfscores.persistence.model.PdfScoreModel

data class PdfScoresCollection(
    val version: Int,
    val authors: List<Author>,
    val pdfScores: List<PdfScoreModel>
)