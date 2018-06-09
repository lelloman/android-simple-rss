package com.lelloman.read.model

data class Article(
        val id: Long,
        val title: String,
        val subtitle: String,
        val time: Long,
        val sourceName: String,
        val sourceId: Long
)