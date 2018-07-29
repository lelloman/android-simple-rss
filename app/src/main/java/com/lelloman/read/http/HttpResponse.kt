package com.lelloman.read.http

data class HttpResponse(
    val code: Int,
    val isSuccessful: Boolean,
    val body: ByteArray
) {
    val stringBody by lazy { String(body) }
}