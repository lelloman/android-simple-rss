package com.lelloman.common.logger

import android.util.Log

class AndroidLogger(private val tag: String) : Logger {

    override fun i(msg: String) {
        Log.i(tag, msg)
    }

    override fun d(msg: String) {
        Log.d(tag, msg)
    }

    override fun w(msg: String, throwable: Throwable?) {
        Log.w(tag, msg, throwable)
    }

    override fun e(msg: String, throwable: Throwable?) {
        Log.e(tag, msg, throwable)
    }
}