package com.lelloman.simplerss.domain_local_sources

interface LocalSource {
    val id: Long
    val name: String
    val url: String
    val lastRefresh: Long
    val isActive: Boolean
    val icon: ByteArray?
    val immutableHashCode: Int
}