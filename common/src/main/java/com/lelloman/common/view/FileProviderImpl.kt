package com.lelloman.common.view

import android.content.Context
import java.io.File
import java.io.InputStream

class FileProviderImpl(
    private val context: Context
) : FileProvider {

    override fun openAssetsFile(fileName: String): InputStream = context
        .assets
        .open(fileName)

    override fun getCacheFile(relativePath: String) = File(context.cacheDir, relativePath)

    override fun getInternalFile(relativePath: String) = File(context.filesDir, relativePath)

    override fun getAbsoluteFile(absolutePath: String) = File(absolutePath)
}