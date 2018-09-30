package com.lelloman.common.utils.model

@Suppress("ArrayInDataClass")
data class ByteArrayWithId(
    val byteArray: ByteArray?,
    val id: Long
)