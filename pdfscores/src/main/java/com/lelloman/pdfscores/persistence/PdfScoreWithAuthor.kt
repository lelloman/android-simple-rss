package com.lelloman.pdfscores.persistence

import com.lelloman.common.utils.model.ModelWithId

data class PdfScoreWithAuthor(
    override var id: Long,
    var fileName: String,
    var created: Long,
    var lastOpened: Long,
    var title: String,
    var authorId: Long
) : ModelWithId