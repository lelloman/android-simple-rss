package com.lelloman.read.http

data class HttpResponse(
    val code: Int,
    val isSuccessful: Boolean,
    val body: ByteArray
) {
    private var stringBodyValue: String? = null

    val stringBody: String
        get() {
            if (stringBodyValue == null) {
                stringBodyValue = String(body)
            }
            return stringBodyValue!!
        }
}