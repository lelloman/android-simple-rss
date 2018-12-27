package com.lelloman.pdfscores.testutils

import com.lelloman.pdfscores.persistence.model.Author
import com.lelloman.pdfscores.persistence.model.PdfScoreModel

fun author(index: Int = 1) = Author(
    firstName = "first named $index",
    lastName = "last named $index",
    isAsset = false,
    collectionId = "collection author $index"
)

fun pdfScore(index: Int = 1) = PdfScoreModel(
    id = 0,
    uri = "uri $index",
    created = index.toLong(),
    lastOpened = index.toLong(),
    title = "title $index",
    authorId = index.toLong(),
    isAsset = false,
    collectionId = "collection pdf $index"
)