package com.lelloman.launcher.logger

import com.lelloman.common.logger.AndroidLogger
import com.lelloman.common.logger.CompoundLogger
import com.lelloman.common.logger.Logger
import com.lelloman.common.logger.LoggerFactory
import com.lelloman.common.logger.PrintWriterLogger
import com.lelloman.common.utils.TimeProvider
import com.lelloman.launcher.LauncherApplication
import com.lelloman.launcher.classification.ClassificationTrigger
import com.lelloman.launcher.classification.PackageClassifier
import com.lelloman.launcher.packages.PackagesManager
import java.io.PrintWriter

class LauncherLoggerFactory(
    private val timeProvider: TimeProvider,
    private val logPrintWriter: PrintWriter
) : LoggerFactory {

    private val fileLoggingClasses = arrayOf(
        PackagesManager::class.java,
        PackageClassifier::class.java,
        LauncherApplication::class.java,
        ClassificationTrigger::class.java
    )

    override fun getLogger(tag: String): Logger {
        TODO("so you want to use the deprecated method uh?")
    }

    override fun getLogger(clazz: Class<*>): Logger = if (fileLoggingClasses.contains(clazz)) {
        CompoundLogger(
            AndroidLogger(clazz.simpleName),
            PrintWriterLogger(clazz.simpleName, timeProvider, logPrintWriter)
        )
    } else {
        AndroidLogger(clazz.simpleName)
    }
}