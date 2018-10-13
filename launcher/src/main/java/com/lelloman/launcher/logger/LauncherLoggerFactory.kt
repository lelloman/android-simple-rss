package com.lelloman.launcher.logger

import com.lelloman.common.logger.AndroidLogger
import com.lelloman.common.logger.CompoundLogger
import com.lelloman.common.logger.Logger
import com.lelloman.common.logger.LoggerFactory
import com.lelloman.common.logger.PrintWriterLogger
import com.lelloman.common.utils.TimeProvider
import java.io.PrintWriter

class LauncherLoggerFactory(
    private val timeProvider: TimeProvider,
    private val logPrintWriter: PrintWriter
) : LoggerFactory {


    override fun getLogger(clazz: Class<*>): Logger = if (ShouldLogToFile::class.java.isAssignableFrom(clazz)) {
        CompoundLogger(
            AndroidLogger(clazz.simpleName),
            PrintWriterLogger(clazz.simpleName, timeProvider, logPrintWriter)
        )
    } else {
        AndroidLogger(clazz.simpleName)
    }
}