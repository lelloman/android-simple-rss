package com.lelloman.domain_sources

interface Source {
    val id: Long
    val name: String
    val url: String
    val lastFetched: Long
    val isActive: Boolean
    val favicon: ByteArray?
    val immutableHashCode: Int
}