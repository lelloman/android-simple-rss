package com.lelloman.read.ui.common.repository

import com.lelloman.read.persistence.db.model.Article
import com.lelloman.read.persistence.db.model.Source

class DeletedSource(
    val source: Source,
    val articles: List<Article>
)