package com.lelloman.common.view

import android.content.Context
import android.net.Uri
import java.io.InputStream

class ContentUriOpener(private val context: Context) {

    fun open(uri: Uri): InputStream? =
        context.contentResolver.openInputStream(uri)
}