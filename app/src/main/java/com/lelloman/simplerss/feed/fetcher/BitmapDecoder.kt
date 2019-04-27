package com.lelloman.simplerss.feed.fetcher

import android.graphics.BitmapFactory

class BitmapDecoder {

    fun decodeBitmap(bitmapBytes: ByteArray) = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.size)
}