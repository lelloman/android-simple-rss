package com.lelloman.common.logger

import com.lelloman.common.utils.TimeProvider
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.*

class PrintWriterLogger(
    private val tag: String,
    private val timeProvider: TimeProvider,
    private val writer: PrintWriter
) : Logger {

    override fun i(msg: String) = writeMessage(msg, "I")

    override fun d(msg: String) = writeMessage(msg, "D")

    override fun w(msg: String, throwable: Throwable?) = writeMessage(msg, "W", throwable)

    override fun e(msg: String, throwable: Throwable?) = writeMessage(msg, "E", throwable)

    private fun writeMessage(msg: String, logType: String, throwable: Throwable? = null) {
        throwable?.printStackTrace(writer)
        val message = "${SDF.format(timeProvider.nowUtcMs())} - $tag[$logType]: $msg"
        writer.write(message)
    }

    private companion object {
        val SDF = SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS", Locale.US)
    }
}