package com.lelloman.common.view

import android.net.Uri
import java.io.InputStream

interface ContentUriOpener {

    fun open(uri: Uri): InputStream?
}