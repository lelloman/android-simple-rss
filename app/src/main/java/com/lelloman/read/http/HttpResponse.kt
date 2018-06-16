package com.lelloman.read.http

data class HttpResponse(
    val code: Int,
    val isSuccessful: Boolean,
    val body: String
)