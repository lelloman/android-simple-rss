package com.lelloman.launcher.logger

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter

class LoggerPrintWriter(context: Context) :
    PrintWriter(
        FileOutputStream(File(context.filesDir, FILE_NAME), true),
        true
    ) {
    private companion object {
        const val FILE_NAME = "log.txt"
    }
}