package com.lelloman.read.utils

import android.arch.lifecycle.MutableLiveData
import kotlin.reflect.KProperty

class LazyLiveData<T>(private val initFunction: (() -> Unit)? = null) {

    private var value: MutableLiveData<T>? = null

    operator fun getValue(myObject: Any, property: KProperty<*>): MutableLiveData<T> {
        if (value == null) {
            value = MutableLiveData()
            initFunction?.invoke()
        }

        return value!!
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T): Nothing =
        throw IllegalAccessException("Property already initialized")
}