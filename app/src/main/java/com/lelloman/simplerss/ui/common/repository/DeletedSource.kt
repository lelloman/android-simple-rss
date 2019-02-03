package com.lelloman.simplerss.ui.common.repository

class DeletedSource(
    val source: com.lelloman.simplerss.persistence.db.model.Source,
    val articles: List<com.lelloman.simplerss.persistence.db.model.Article>
)