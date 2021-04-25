package com.lelloman.simplerss.domain_local_sources

interface LocalSourceItem {
    val id: Long
    val title: String
    val subtitle: String
    val content: String
    val link: String
    val imageUrl: String?
    val time: Long
}