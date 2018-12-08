package com.lelloman.common.view

import java.io.File
import java.io.InputStream

interface FileProvider {

    fun openAssetsFile(fileName: String): InputStream

    fun getCacheFile(relativePath: String): File

    fun getInternalFile(relativePath: String): File

    fun getAbsoluteFile(absolutePath: String): File
}