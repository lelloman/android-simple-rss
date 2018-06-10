package com.lelloman.read.sourceslist.model

import com.lelloman.read.core.ModelWithId

data class Source(
    override val id: Long,
    val name: String,
    val url: String
) : ModelWithId