package com.lelloman.common.utils

import android.util.Base64

object Base64Util {

    fun decode(string: String): ByteArray = Base64.decode(string, 0)

    fun encode(byteArray: ByteArray): String = String(Base64.encode(byteArray, 0))
}