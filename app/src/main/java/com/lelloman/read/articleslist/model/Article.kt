package com.lelloman.read.articleslist.model

import com.lelloman.read.core.ModelWithId

data class Article(
    override val id: Long,
    val title: String,
    val subtitle: String,
    val time: Long,
    val sourceName: String,
    val sourceId: Long
) : ModelWithId