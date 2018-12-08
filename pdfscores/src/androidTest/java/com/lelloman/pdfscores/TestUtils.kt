package com.lelloman.pdfscores

import com.lelloman.pdfscores.persistence.model.Author
import com.lelloman.pdfscores.persistence.model.PdfScoreModel


fun pdfScore(index: Int = 1) = PdfScoreModel(
    id = 0,
    uri = "uri $index",
    created = index.toLong(),
    lastOpened = index.toLong(),
    title = "title $index",
    authorId = index.toLong()
)

fun author(index: Int = 1) = Author(
    id = 0L,
    firstName = "firstName $index",
    lastName = "lastName $index"
)