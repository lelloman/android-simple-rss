package com.lelloman.pdfscores.persistence.model

import com.lelloman.common.utils.model.ModelWithId

data class PdfScoreWithAuthor(
    override var id: Long,
    var uri: String,
    var created: Long,
    var lastOpened: Long,
    var title: String,
    var authorId: Long
) : ModelWithId