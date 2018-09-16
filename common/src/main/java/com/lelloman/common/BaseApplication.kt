package com.lelloman.common

import android.app.Application

abstract class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        inject()
    }

    protected abstract fun inject()

    companion object {
        private lateinit var instance: BaseApplication

    }
}