package com.lelloman.simplerss.ui.common.repository

import com.lelloman.simplerss.persistence.db.model.Article
import com.lelloman.simplerss.persistence.db.model.Source

class DeletedSource(
    val source: Source,
    val articles: List<Article>
)