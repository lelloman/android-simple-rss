package com.lelloman.domain_sources

interface SourceItem {
    val sourceItemId: String
    val title: String
    val subtitle: String
    val content: String
    val link: String
    val imageUrl: String?
    val time: Long
    val sourceId: String
    val sourceName: String
    val icon: ByteArray?
}