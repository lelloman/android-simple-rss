package com.lelloman.simplerss.feed

data class ParsedFeed(
    val title: String,
    val subtitle: String,
    val link: String,
    val timestamp: Long
)