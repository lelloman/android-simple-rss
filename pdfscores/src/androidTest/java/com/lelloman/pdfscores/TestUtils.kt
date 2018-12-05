package com.lelloman.pdfscores

import com.lelloman.pdfscores.persistence.Author
import com.lelloman.pdfscores.persistence.PdfScore


fun pdfScore(index: Int = 1) = PdfScore(
    id = 0,
    fileName = "fileName $index",
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