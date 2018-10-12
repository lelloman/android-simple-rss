package com.lelloman.launcher.logger

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.*

class LoggerPrintWriter(context: Context) :
    PrintWriter(
        FileOutputStream(File(context.filesDir, FILE_NAME), true),
        true
    ) {
    private companion object {
        val SDF = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US)
        val FILE_NAME get() = "log.txt"//"${SDF.format(System.currentTimeMillis())}.log"
    }
}