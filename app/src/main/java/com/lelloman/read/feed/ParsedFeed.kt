package com.lelloman.read.feed

data class ParsedFeed(
    val title: String,
    val subtitle: String,
    val link: String,
    val timestamp: Long
)