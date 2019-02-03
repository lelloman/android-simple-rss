package com.lelloman.simplerss.http

@Suppress("ArrayInDataClass")
data class HttpResponse(
    val code: Int,
    val isSuccessful: Boolean,
    val body: ByteArray = byteArrayOf()
) {
    val stringBody by lazy { String(body) }
}