package com.lelloman.launcher.logger

import android.content.Context
import java.io.File
import java.io.PrintWriter

class LoggerPrintWriter(context: Context) :
    PrintWriter(File(context.filesDir, LOG_FILE_NAME)) {
    private companion object {
        const val LOG_FILE_NAME = "Log.txt"
    }
}