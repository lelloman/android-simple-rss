package com.lelloman.read.feed.finder

import com.lelloman.read.core.ModelWithId

data class FoundFeed(
    override val id: Long,
    val url: String,
    val nArticles: Int,
    var name: String? = null
) : ModelWithId