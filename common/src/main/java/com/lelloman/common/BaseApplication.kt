package com.lelloman.common

import android.app.Application
import android.content.Context

abstract class BaseApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        instance = this
        inject()
    }

    protected abstract fun inject()

    companion object {
        private lateinit var instance: BaseApplication

    }
}