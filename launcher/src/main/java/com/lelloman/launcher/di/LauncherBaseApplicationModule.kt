package com.lelloman.launcher.di

import android.app.Application
import com.lelloman.common.di.BaseApplicationModule
import com.lelloman.common.logger.Logger
import com.lelloman.common.logger.LoggerFactory
import com.lelloman.common.logger.LoggerFactoryImpl

class LauncherBaseApplicationModule(application: Application) :
    BaseApplicationModule(application) {

    override fun provideLoggerFactory() = object : LoggerFactory {
        private val actual = LoggerFactoryImpl()

        override fun getLogger(clazz: Class<*>): Logger {
            if (clazz.name.startsWith("com.lelloman.launcher")) {
                throw IllegalAccessException("Class ${clazz.name} should be using LauncherLoggerFactory.")
            }
            return actual.getLogger(clazz)
        }
    }
}