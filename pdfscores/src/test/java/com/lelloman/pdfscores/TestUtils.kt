package com.lelloman.pdfscores

import com.lelloman.pdfscores.persistence.model.Author
import com.lelloman.pdfscores.persistence.model.PdfScoreModel

fun author(index: Int = 1) = Author(
    firstName = "first named $index",
    lastName = "last named $index",
    isAsset = false,
    id = "collection author $index"
)

fun pdfScore(index: Int = 1) = PdfScoreModel(
    uri = "uri $index",
    created = index.toLong(),
    lastOpened = index.toLong(),
    title = "title $index",
    authorId = "author $index",
    isAsset = false,
    id = "collection pdf $index"
)