package com.lelloman.read.feed

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.lelloman.common.logger.LoggerFactory

class FaviconBitmapProvider(
    loggerFactory: LoggerFactory
) {
    private val logger = loggerFactory.getLogger(FaviconBitmapProvider::class.java)
    private val cache = mutableMapOf<Long, Bitmap?>()

    fun getFaviconBitmap(byteArray: ByteArray, id: Long): Bitmap? {
        if (cache.containsKey(id)) {
            logger.d("returning favicon $id from cache")
            return cache[id]
        }

        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size).apply {
            cache[id] = this
            logger.d("returning favicon $id from BitmapFactory")
        }
    }
}